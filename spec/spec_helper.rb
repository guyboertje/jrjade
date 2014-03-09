# require 'bundler'
# Bundler.setup

require 'minitest/autorun'
require 'minitest/mock'
require 'minitest/pride'

require_relative '../lib/jrjade'

Itemish = Struct.new(:name, :current, :url)

class Exhib
  def label(num) "something #{num}"; end
  def class_for(num) "line-#{num}"; end
  def url() "some/path/to/resource"; end
end

module Context
  extend self

  def header() "Colors"; end
  def item
    [
      Itemish.new("red", true, "#red"),
      Itemish.new("green", false, "#green"),
      Itemish.new("blue", false, "#blue"),
    ]
  end
  def exhibit() Exhib.new; end
  def foo() "FOO"; end
  def bar() "BAR"; end
  def baz() "BAZ"; end
  def fool() "/foo"; end
  def i() 4; end

  def get_binding() binding; end
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
