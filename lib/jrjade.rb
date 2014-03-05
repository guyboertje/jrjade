unless RUBY_PLATFORM =~ /java/
  puts "This library is only compatible with a java-based ruby environment like JRuby."
  exit 255
end

# require_relative "jars/jade4j-0.4.1.jar"
require "jars/jade4j-0.4.1-SNAPSHOT.jar"

require "jrjade/version"
require "jrjade/jade_compiler"
require "jrjade/jade_engine"
require "jrjade/jade_template"
