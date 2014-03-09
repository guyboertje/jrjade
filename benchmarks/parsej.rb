def parse
  # str = %q[]
  str = %q[<h1>Hello World!</h1>
<a href="%(*=fool*)%">%(*=baz*)%</a>
<ul class="class-top">
  <li class="blat">%(*=baz*)%</li>
</ul>+:*^0*i.times do |n|*:++:*quux = exhibit.class_for(n)*:+
  <p class="%(*=quux*)%">%(*=foo*)%</p>
  <a href="%(*=exhibit.url*)%">blue</a>
  <a href="/green">green</a>+:*^1*if n == 2*:+
    <p>%(*=bar*)%</p>*1^
  +:*^1*elsif n == 3*:+
    <div class="three">THREE %(*=n*)%</div>
    <div class="three">THREE %(*n*)%</div>*1^
  +:*^1*else*:+
    <div class="elsie">ELSIE</div>
    <a href="/elsie">Clink heare</a>*1^
  *0^
+:*qua = exhibit.label(i)*:++:*qub = exhibit.label(i.succ)*:+
<h2>%(*=qua*)%</h2>
<h2>%(*=qub*)%</h2>
<h3>%(*Time.now.strftime("%F %R")*)%</h3>]
  
  exmap = {
    "*:++:*" => "; ",
    "+:*" => "]; ",
    "*:+" => "; j_j.concat %Q[",
  }
  cre = %r{%\(\*=?(.*?)\*\)%}
  cma = ']; j_j.concat((\1).to_s); j_j.concat %Q['
  re = Regexp.new(exmap.keys.map { |x| Regexp.escape(x) }.join('|'))
  rer = %r[\*(\d+)\^(\s*?)\+\:\*\^\1\*] # '*1^  +:*^1*'

  inter = str.gsub(rer, '\2]; ').gsub(cre, cma).gsub(re, exmap).gsub(%r[\^\d+\*], "").gsub(%r[\*\d+\^], "]; end;  j_j.concat %Q[")
 
  "j_j=''; j_j.concat %Q[" + inter + "]; j_j"
end

class Exhib
  def label(num) "something #{num}"; end
  def class_for(num) "line-#{num}"; end
  def url() "some/path/to/resource"; end
end

exhibit = Exhib.new
foo = "FOO"
bar = "BAR"
baz = "BAZ"
fool = "/foo"
i = 4

# puts parse
puts Kernel.eval(parse())
