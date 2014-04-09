require 'ripper'
require 'pry'
require 'pry-byebug'

def parse
  # str = %q[]
  str = %q{j_j = ''; ;;0; if exhibit.allowed?; j_j.concat %Q[
  <h1>Hello World!</h1>
  <!--link to fool and baz-->
  <a href="]; j_j.concat((fool).to_s); j_j.concat %Q[">]; j_j.concat((baz).to_s); j_j.concat %Q[</a>
  <p>Get on it</p><h1>Markdown</h1>
  <ul class="class-top">
    <li class="blat">]; j_j.concat((baz).to_s); j_j.concat %Q[</li>
  </ul>]; ;;1; i.times do |n|; j_j.concat %Q[]; quux = exhibit.class_for(n); j_j.concat %Q[
    <p class="]; j_j.concat((quux).to_s); j_j.concat %Q[">]; j_j.concat((foo).to_s); j_j.concat %Q[</p>
    <a href="]; j_j.concat((exhibit.url).to_s); j_j.concat %Q[">blue</a>
    <a href="/green">green</a>]; ;;2; if n == 2; j_j.concat %Q[
      <p>]; j_j.concat((bar).to_s); j_j.concat %Q[</p>]; ;2;; j_j.concat %Q[
    ]; ;;2; elsif n == 3; j_j.concat %Q[
      <div class="three">THREE ]; j_j.concat((n).to_s); j_j.concat %Q[</div>
      <div class="three">THREE ]; j_j.concat((n).to_s); j_j.concat %Q[</div>]; ;2;; j_j.concat %Q[
    ]; ;;2; else; j_j.concat %Q[
      <div class="elsie">ELSIE</div>
      <a href="/elsie">Click ]; j_j.concat((bar).to_s); j_j.concat %Q[</a>]; ;2;; j_j.concat %Q[
    ]; ;1;; j_j.concat %Q[
  ]; qua = exhibit.label(i); j_j.concat %Q[]; qub = exhibit.label(i.succ); j_j.concat %Q[
  <h2>]; j_j.concat((qua).to_s); j_j.concat %Q[</h2>
  <h2>]; j_j.concat((qub).to_s); j_j.concat %Q[</h2>]; ;;1; if !ex.nil?; j_j.concat %Q[
    <p class="pres"></p>]; ;1;; j_j.concat %Q[
  ]; ;;1; else; j_j.concat %Q[
    <p class="not-pres"></p>]; ;1;; j_j.concat %Q[
  
  <h3>]; j_j.concat((Time.now.strftime("%F %R")).to_s); j_j.concat %Q[</h3>
  <div>a</div>
  <div>b</div>
  <div>c</div>
  <div>d</div>
  <div>e</div>]; ;0;; j_j.concat %Q[
]; ;;0; else; j_j.concat %Q[
  <p class="blocked">Access denied</p>]; ;0;;  j_j;
  }

  levels = str.scan(/;;(\d+);/).flatten.compact.sort.uniq
  treat(str, levels)
end

def treat(str, levels)
  sl = levels.shift
  level = sl.to_i
  rex = Regexes.new(level)
  parts = str.partition(rex.parts)
  if levels.size > 0
    # binding.pry
    parts[1] = treat(parts[1], levels)
  end
  parts[1] = __treat(parts[1], rex)
  # binding.pry
  parts.join
end

def __treat(subj, rex)
  looping = 0
  while !parses?(subj) do
    fail "Unable to parse template" if looping > 99
    looping += 1
    re = rex.choose(subj)
    pieces = subj.partition(re)
    pieces[1].sub!(rex.for_end, 'end;').gsub!(rex.for_all, '')
    subj = pieces.join
  end
  subj
end

class Regexes
  def untreated_if; @luu; end

  def parts; @lup; end

  def for_ifelse; @lui; end

  def for_rest; @lud; end

  def for_end; @lue; end

  def for_all; @lua; end

  def choose(str)
    str =~ untreated_if ? for_ifelse : for_rest
  end

  def initialize(num)
    @lup = /;;#{num};.+;#{num};;/m
    @lui = /;;#{num};\s*if.+els(e|if).+?;#{num};;/m
    @lud = /;;#{num};.+?;#{num};;/m
    @lue = /;#{num};;\z/m
    @lua = /;;#{num};|;#{num};;/
    @luu = /;;#{num};\s*if/
  end
end

def parses?(str)
  !!Ripper::SexpBuilder.parse(str)
end

class Exhib
  def label(num) "something #{num}"; end
  def class_for(num) "line-#{num}"; end
  def url() "some/path/to/resource"; end
  def allowed?() true; end
end

exhibit = Exhib.new
foo = "FOO"
bar = "BAR"
baz = "BAZ"
fool = "/foo"
ex = 3
i = 4

# puts parse
puts Kernel.eval(parse())
