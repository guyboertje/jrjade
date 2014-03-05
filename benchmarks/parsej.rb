def parse
  # str = %q[]
  str = %q[<h1>Hello World!</h1>
<a href="%(*fool*)%">%(*baz*)%</a>
<ul class="class-top">
  <li class="blat">%(*baz*)%</li>
</ul>+:*^0*i.times do |n|*:++:*quux = exhibit.class_for(n)*:+
  <p class="quux">%(*foo*)%</p>+:*^1*if n == 2*:+
    <p>%(*bar*)%</p>*1^
  +:*^1*elsif n == 3*:+
    <div class="three">THREE #{n}</div>*1^
  +:*^1*else*:+
    <div class="elsie">ELSIE</div>
    <a href="/elsie">Clink heare</a>*1^
  *0^
+:*qua = exhibit.label(i)*:++:*qub = exhibit.label(i.succ)*:+
<h2>%(*qua*)%</h2>
<h2>%(*qub*)%</h2>]
  
  exmap = {
    "*:++:*" => "; ",
    "+:*" => "]; ",
    "*:+" => "; j_j.concat %Q[",
    "%(*" => "]; j_j.concat((",
    "*)%" => ").to_s); j_j.concat %Q["
  }

  re = Regexp.new(exmap.keys.map { |x| Regexp.escape(x) }.join('|'))
  rer = %r[\*(\d+)\^(\s*)\+\:\*\^\1\*]

  inter = str.gsub(rer, '\2]; ').gsub(re, exmap).gsub(%r[\^\d+\*], "").gsub(%r[\*\d+\^], "]; end;  j_j.concat %Q[")
 
  "j_j=''; j_j.concat %Q[" + inter + "]; j_j"
end

class Exhib
  def label(num)
    "something #{num}"
  end

  def class_for(num)
    "line-#{num}"
  end
end

exhibit = Exhib.new
foo = "FOO"
bar = "BAR"
baz = "BAZ"
fool = "/foo"
i = 4

puts Kernel.eval(parse())
# puts parse
