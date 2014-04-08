module JrJade
  class JadeEngine
    def self.render(string, options = {})
      level = options[:safe_level]
      instance = self.new(string, level, options)
      instance.result
    end

    attr_accessor :filename

    def initialize(str, safe_level=nil, options = {})
      @safe_level = safe_level
      @filename = options[:path]
      @outvar = options[:outvar]
      @compiler = JadeCompiler.new(str, options)
    end

    def set_outvar(outvar)
      @outvar = outvar
      @compiler.set_outvar(outvar)
    end

    def src
      if @compiler.ensure_outvar(@outvar)
        @src ||= @compiler.convert
      else
        @src = @compiler.convert
      end
      @src
    end

    def run(b=new_toplevel)
      print self.result(b)
    end

    def result(b=new_toplevel)
      if @safe_level
        proc {
          $SAFE = @safe_level
          eval(src, b, (@filename || '(jade)'), 0)
        }.call
      else
        eval(src, b, (@filename || '(jade)'), 0)
      end
    end

    def template
      src
    end

    private

    def new_toplevel
      TOPLEVEL_BINDING.dup
    end
  end
end
