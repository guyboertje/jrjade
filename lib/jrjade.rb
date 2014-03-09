unless RUBY_PLATFORM =~ /java/
  puts "This library is only compatible with a java-based ruby environment like JRuby."
  exit 255
end

require "jars/jade4j-0.4.2-SNAPSHOT.jar"

require "jrjade/version"
require "jrjade/jade_compiler"
require "jrjade/jade_engine"
require "jrjade/jade_template"
