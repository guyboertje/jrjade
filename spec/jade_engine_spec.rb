require_relative 'spec_helper'

describe "JrJade::JadeEngine" do
  before do
    @klass = JrJade::JadeEngine
  end

  describe 'class methods' do
    it "#render" do
      @klass.must_respond_to 'render'
    end
  end
  
end
