jrjade
======

JRuby wrapper for Jade4j with Tilt template

I have modified the original jade4j java library to allow for contextual evaluation in ruby

In ```+link(fool, baz)``` the values for fool and baz are sourced from the ruby context.

In ```p(class=quux)= foo``` the values for quux and foo are sourced from the ruby context.

example template:
```jade
mixin link(href, name)
  a(href=href)= name
!!! 5
html
  head
    title Simple Benchmark
  body
    h1 Hello World!
    // link to fool and baz
    +link(fool, baz)
    p.
      Get on it
    :markdown
      # Markdown
    ul.class-top
      li.blat= baz
    - i.times do |n|
      - quux = exhibit.class_for(n)
      p(class=quux)= foo
      a(href=exhibit.url) blue
      a(href='/green') green
      - if n == 2
        p= bar
      - elsif n == 3
        .three THREE #{n}
        .three THREE !{n}
      - else
        .elsie ELSIE
        +link('/elsie', 'Click #{bar}')
    - qua, qub = exhibit.label(i), exhibit.label(i.succ)
    h2= qua
    h2= qub
    include includes/footer
```
