java_import 'de.neuland.jade4j.model.JadeModel'

module JrJade
  class ContextModel < JadeModel
    def self.hashify(context, locals)
      inst = new(locals)
      inst.add_context(context)
    end

    def add_context(context)
      @context = context
      self
    end

    def get(key)
      value = super
      k = key.to_s
      if value.nil? && @context.respond_to?(k)
        value = @context.send(k)
      end
      value
    end
  end
end
