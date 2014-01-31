# encoding: utf-8
require_relative 'spec_helper'

describe "JrJade::ContextModel" do
  before do
    @klass = JrJade::ContextModel
  end

  describe 'class methods' do
    it "#hashify" do
      @klass.must_respond_to 'hashify'
    end
  end

  describe 'instance methods' do
    before do
      @instance = @klass.new({})
    end

    it "#add_context" do
      @instance.must_respond_to 'add_context'
    end
    
    it "#get" do
      @instance.must_respond_to 'get'
    end
    
    it "#put" do
      @instance.must_respond_to 'put'
    end
  end

  describe "state" do
    before do
      @context = JrJade::Some.new("I", "II", "III")
      @locals = { "foo" => "FOO", "bar" => "BAR", "baz" => "BAZ", "quux" => "QUUX" }
      @instance = @klass.hashify(@context, @locals)
      @instance.put("żółć", "UTF8")
    end

    it "finds context values" do
      @instance.get('one').must_equal "I"
      @instance.get('rest').five.must_equal "V"
    end

    it "finds local values" do
      @instance.get('bar').must_equal "BAR"
    end

    it "finds added values" do
      @instance.get('żółć').must_equal "UTF8"
    end
  end
end
