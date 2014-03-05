require "tilt/template"

module JrJade
  class JadeTemplate < Tilt::Template
    self.default_mime_type = 'text/html'

    def self.engine_initialized?
      true
    end

    def initialize_engine
    end

    @@default_output_variable = '_jadeout'

    def self.default_output_variable
      @@default_output_variable
    end

    def self.default_output_variable=(name)
      warn "#{self}.default_output_variable= has been replaced with the :outvar-option"
      @@default_output_variable = name
    end

    def prepare
      options[:outvar] ||= self.class.default_output_variable
      @outvar = options[:outvar]
      opts = options.merge(path: file)
      @engine = JrJade::JadeEngine.new(data, nil, opts)
    end

    def precompiled_template(locals)
      @engine.set_outvar(@outvar)
      source = @engine.src
      source
    end

    def precompiled_preamble(locals)
      <<-RUBY
        begin
          __original_outvar = #{@outvar} if defined?(#{@outvar})
          #{super}
      RUBY
    end

    def precompiled_postamble(locals)
      <<-RUBY
          #{super}
        ensure
          #{@outvar} = __original_outvar
        end
      RUBY
    end

    private

    def read_template_file
      nil # let java load the file
    end
  end
end
