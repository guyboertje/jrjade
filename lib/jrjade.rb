unless RUBY_PLATFORM =~ /java/
  puts 'This library is only compatible with a java-based ruby environment like JRuby.'
  exit 255
end

# require 'jars/jade4j-0.4.2-SNAPSHOT.jar'
require '../target/jrjade-0.4.2.jar'
require 'com/jrjade/jrjade'

require 'jrjade/version'
require 'jrjade/jade_template'

# require './target/jrjade-0.4.2.jar'; require 'com/jrjade/jrjade'
