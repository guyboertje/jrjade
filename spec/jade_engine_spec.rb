require_relative 'spec_helper'

describe "JrJade::JadeEngine" do
  before do
    @klass = JrJade::JadeEngine
  end

  describe 'class methods' do
    it "has a render method" do
      @klass.must_respond_to 'render'
    end

    it "renders a jade template without variables" do
      source = JrJade.read_file('prettyprint.jade')
      compare = @klass.render(source, {pretty: true})
      compare.must_equal JrJade.read_file('prettyprint.html').strip
    end

    it "renders a jade template with variables" do
      source = JrJade.read_file('view1.jade')
      compare = @klass.new(source, nil, pretty: true).result(Context.get_binding).squeeze(" ")
      compare.must_equal JrJade.read_file('view1.html').strip.squeeze(" ")
    end

    it "renders a jade template string with variables" do
      source = JrJade.read_file('view.jade')
      compare = @klass.new(source, nil, pretty: true).result(Context.get_binding).squeeze(" ")
      compare.must_equal JrJade.read_file('view.html').strip.squeeze(" ")
    end

    it "renders a jade template file with variables" do
      path = JrJade.resource_file('view.jade')
      options = {pretty: true, path: path}
      compare = @klass.new(nil, nil, options).result(Context.get_binding).squeeze(" ")
      compare.must_equal JrJade.read_file('view.html').strip.squeeze(" ")
    end

  end
end
