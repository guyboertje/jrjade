java_import 'de.neuland.jade4j.Jade4J'
require 'digest'

module JrJade
  class JadeCompiler
    attr_reader :pretty

    Excre = %r{%\(\*=?(.*?)\*\)%}
    Exscc = "*:++:*"
    Expcs = "+:*"
    Exscp = "*:+"
    Exmap = [ Exscc, Expcs, Exscp ]
    Exsis = "; "
    Exbis = "]; "

    Exre  = Regexp.new(Exmap.map { |x| Regexp.escape(x) }.join('|'))
    Exer  = %r[\*(\d+)\^(\s*)\+\:\*\^\1\*]
    Exmer = "\\2]; "
    Excaret = %r[\^\d+\*]
    Exstar = %r[\*\d+\^]

    def initialize(src, options = {})
      @pretty = options.fetch(:pretty, true)
      @path = options[:path]
      @substitutions_built = false
      @raw = Jade4J.render(template(src), @pretty, true)
      ensure_outvar(options[:outvar])
    end

    def convert
      staged = @raw.gsub(Exer, Exmer).gsub(Excre, @excma).gsub(Exre, @exmap).gsub(Excaret, "").gsub(Exstar, @exstar)
      @out_pre + staged + @out_suf
    end

    def set_outvar(outvar)
      @outvar = outvar
      build_substitutions
    end

    def ensure_outvar(outvar)
      return true if substitutions_built? && @outvar == outvar
      set_outvar outvar || '_jadeout'
      false
    end

    private

    def template(src)
      src.nil? ? file_template : src_template(src)
    end

    def src_template(src)
      Jade4J.getTemplate(
        reader(src),
        name(src)
      )
    end

    def reader(string)
      Java::JavaIo::StringReader.new(string)
    end

    def name(string)
      @path || Digest::SHA256.hexdigest(string)
    end

    def file_template
      Jade4J.getTemplate(@path)
    end

    def substitutions_built?
      @substitutions_built
    end

    def build_substitutions
      @exmap = Hash[ Exmap.zip([Exsis,Exbis, "; #{@outvar}.concat %Q["]) ]
      @exstar = "]; end;  #{@outvar}.concat %Q["
      @excma = "]; #{@outvar}.concat((\\1).to_s); #{@outvar}.concat %Q["
      @out_pre = "#{@outvar}=''; #{@outvar}.concat %Q["
      @out_suf = "]; #{@outvar}"

      # "]; #{@outvar}.force_encoding(__ENCODING__)"
      @substitutions_built = true
    end
  end
end
