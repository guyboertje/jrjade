#!/usr/bin/env ruby

$:.unshift(File.join(File.dirname(__FILE__), '..', 'lib'), File.dirname(__FILE__))
require 'context'

require 'benchmark'
require 'tilt'
require 'erb'
require 'slim'
require 'jrjade'

class JadeBenchmarks
  def initialize(slow, iterations)
    # @iterations = (iterations || 10000).to_i
    @iterations = (iterations || 1000).to_i
    @benches    = []

    @jade_file = File.dirname(__FILE__) + '/view.jade'

    @jade_code = File.read(@jade_file)
    @slim_code = File.read(File.dirname(__FILE__) + '/view.slim')
    @erb_code = File.read(File.dirname(__FILE__) + '/view.erb')

    @jade_opts = {pretty: true, path: @jade_file}

    init_compiled_benches
    init_tilt_benches
    # init_parsing_benches
    init_rendering_benches

    # init_parsing_benches if slow
  end

  def init_compiled_benches
    context  = Context.new

    context.instance_eval %{
      def run_erb; #{ERB.new(@erb_code).src}; end
      def run_jade_pretty; #{JrJade::JadeEngine.new(@jade_code, nil, @jade_opts).src}; end
      def run_slim_pretty; #{ Slim::Engine.new(:pretty => true).call(@slim_code)}; end
    }

    bench('(1) slim pretty') { context.run_slim_pretty }
    bench('(1) erb')         { context.run_erb }
    bench('(1) jade pretty') { context.run_jade_pretty }

    bench('(1a) slim pretty') { Slim::Engine.new(:pretty => true).call(@slim_code) }
    bench('(1a) erb')         { ERB.new(@erb_code).src }
    bench('(1a) jade pretty') { JrJade::JadeEngine.new(@jade_code, nil, @jade_opts).src }
  end

  def init_tilt_benches
    tilt_erb        = Tilt::ERBTemplate.new { @erb_code }
    tilt_slim_pretty= Slim::Template.new(pretty: true) { @slim_code }
    tilt_jade       = JrJade::JadeTemplate.new(@jade_file, 1, pretty: true)

    context  = Context.new

    # bench('(2) slim pretty') { tilt_slim_pretty.render(context) }
    bench('(2) erb')         { tilt_erb.render(context) }
    bench('(2) jade pretty') { tilt_jade.render(context) }
  end

  def init_parsing_benches
    context  = Context.new
    context_binding = context.instance_eval { binding }
    # bench('(3) slim pretty') { Slim::Template.new(:pretty => true) { @slim_code }.render(context) }
    bench('(3) erb')         { ERB.new(@erb_code).result(context_binding) }
    bench('(3) jade pretty') { JrJade::JadeEngine.new(nil, nil, @jade_opts).result(context_binding) }
  end

  def init_rendering_benches
    context  = Context.new
    context_binding = context.instance_eval { binding }

    erb = ERB.new(@erb_code)
    slim = Slim::Template.new(:pretty => true) { @slim_code }
    jade = JrJade::JadeEngine.new(nil, nil, @jade_opts).tap{|j| j.src}
    # bench('(4) slim pretty') { slim.render(context) }
    bench('(4) erb')         { erb.result(context_binding) }
    bench('(4) jade pretty') { jade.result(context_binding) }
  end

  def run
    puts "#{@iterations} Iterations"
    Benchmark.bmbm do |x|
      @benches.each do |name, block|
        x.report name.to_s do
          @iterations.to_i.times { block.call }
        end
      end
    end
    puts "
(1) Compiled benchmark. Template is parsed before the benchmark and
    generated ruby code is compiled into a method.
    This is the fastest evaluation strategy because it benchmarks
    pure execution speed of the generated ruby code.

(2) Compiled Tilt benchmark. Template is compiled with Tilt, which gives a more
    accurate result of the performance in production mode in frameworks like
    Sinatra, Ramaze and Camping. (Rails still uses its own template
    compilation.)

(3) Parsing benchmark. Template is parsed every time.
    This is not the recommended way to use the template engine
    and Slim is not optimized for it. Activate this benchmark with 'rake bench slow=1'.

(4) Rendering benchmark. Template is compiled before and rendered in context every time.

Temple ERB is the ERB implementation using the Temple framework. It shows the
overhead added by the Temple framework compared to ERB.
"
  end

  def bench(name, &block)
    @benches.push([name, block])
  end
end

JadeBenchmarks.new(ENV['slow'], ENV['iterations']).run
