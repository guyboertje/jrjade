# -*- encoding: utf-8 -*-
$:.push File.expand_path("../lib", __FILE__)
require "version"

Gem::Specification.new do |s|
  s.name        = "jrjade"
  s.version     = Jrjade::VERSION
  s.authors     = ["Guy Boertje"]
  s.email       = ["guyboertje@gmail.com"]
  s.homepage    = "https://github.com/guyboertje/jrjade"
  s.summary     = %q{jade4j wrapper tilt template gem}
  s.description = %q{jade4j wrapper tilt template gem}

  s.files         = `git ls-files`.split($/)
  s.extensions    = []
  s.test_files    = `git ls-files -- {test,spec,features}/*`.split("\n")
  s.executables   = `git ls-files -- bin/*`.split("\n").map{ |f| File.basename(f) }
  s.require_paths = ["lib"]

  # specify any dependencies here; for example:
  # s.add_development_dependency "rspec"
  s.add_dependency "tilt"
end
