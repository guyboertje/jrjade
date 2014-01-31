require "tilt/template"

module JrJade
  class JadeTemplate < Tilt::Template
    self.default_mime_type = 'text/html'

    def self.engine_initialized?
      true
    end

    def initialize_engine
    end

    def prepare
    end

    def evaluate(scope, locals, &block)
      context = ContextModel.hashify(scope, locals)
      JadeEngine.render(file, context, options)
    end

    private

    def read_template_file
      ""
    end
  end
end
