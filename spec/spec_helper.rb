# require 'bundler'
# Bundler.setup

require 'minitest/autorun'
require 'minitest/mock'
require 'minitest/pride'

require_relative '../lib/jrjade'

module JrJade
  Rest = Struct.new(:four, :five, :six, :seven)
  Some = Struct.new(:one, :two, :three) do
    def rest
      @four ||= Rest.new("IV", "V", "VI", "VII")
    end
  end
end
