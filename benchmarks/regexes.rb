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
