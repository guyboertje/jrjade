unless RUBY_PLATFORM =~ /java/
  puts "This library is only compatible with a java-based ruby environment like JRuby."
  exit 255
end

require_relative "jars/jade4j-0.4.1.jar"
# require_relative "linked/jrjackson-1.2.8.jar"

require_relative "jrjade/version"
require_relative "jrjade/context_model"
require_relative "jrjade/jade_engine"
require_relative "jrjade/jade_template"
