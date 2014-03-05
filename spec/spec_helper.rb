# require 'bundler'
# Bundler.setup

require 'minitest/autorun'
require 'minitest/mock'
require 'minitest/pride'

require_relative '../lib/jrjade'


Itemish = Struct.new(:name, :current, :url)

module Context
  def self.header
    "Colors"
  end
  def self.item
    [
      Itemish.new("red", true, "#red"),
      Itemish.new("green", false, "#green"),
      Itemish.new("blue", false, "#blue"),
    ]
  end

  def self.get_binding
    return binding
  end
end

module JrJade
  def self.resource_file(file)
    File.join(File.dirname(__FILE__), 'resources', file)
  end

  def self.read_file(file)
    path = resource_file(file)
    File.read(path)
  end

end
