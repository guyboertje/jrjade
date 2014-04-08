

import org.jruby.Ruby;
import org.jruby.RubyObject;
import org.jruby.runtime.Helpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.RubyClass;


public class Regexes extends RubyObject  {
    private static final Ruby __ruby__ = Ruby.getGlobalRuntime();
    private static final RubyClass __metaclass__;

    static {
        String source = new StringBuilder("class Regexes\n" +
            "  def untreated_if; @luu; end\n" +
            "\n" +
            "  def parts; @lup; end\n" +
            "\n" +
            "  def for_ifelse; @lui; end\n" +
            "\n" +
            "  def for_rest; @lud; end\n" +
            "\n" +
            "  def for_end; @lue; end\n" +
            "\n" +
            "  def for_all; @lua; end\n" +
            "\n" +
            "  def choose(str)\n" +
            "    str =~ untreated_if ? for_ifelse : for_rest\n" +
            "  end\n" +
            "\n" +
            "  def initialize(num)\n" +
            "    @lup = /;;#{num};.+;#{num};;/m\n" +
            "    @lui = /;;#{num};\\s*if.+els(e|if).+?;#{num};;/m\n" +
            "    @lud = /;;#{num};.+?;#{num};;/m\n" +
            "    @lue = /;#{num};;\\z/m\n" +
            "    @lua = /;;#{num};|;#{num};;/\n" +
            "    @luu = /;;#{num};\\s*if/\n" +
            "  end\n" +
            "end\n" +
            "").toString();
        __ruby__.executeScript(source, "./regexes.rb");
        RubyClass metaclass = __ruby__.getClass("Regexes");
        if (metaclass == null) throw new NoClassDefFoundError("Could not load Ruby class: Regexes");
        metaclass.setRubyStaticAllocator(Regexes.class);
        __metaclass__ = metaclass;
    }

    /**
     * Standard Ruby object constructor, for construction-from-Ruby purposes.
     * Generally not for user consumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    private Regexes(Ruby ruby, RubyClass metaclass) {
        super(ruby, metaclass);
    }

    /**
     * A static method used by JRuby for allocating instances of this object
     * from Ruby. Generally not for user comsumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    public static IRubyObject __allocate__(Ruby ruby, RubyClass metaClass) {
        return new Regexes(ruby, metaClass);
    }

    
    public Object untreated_if() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "untreated_if");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object parts() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "parts");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object for_ifelse() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "for_ifelse");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object for_rest() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "for_rest");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object for_end() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "for_end");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object for_all() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "for_all");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object choose(Object str) {
        IRubyObject ruby_arg_str = JavaUtil.convertJavaToRuby(__ruby__, str);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "choose", ruby_arg_str);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public  Regexes(Object num) {
        this(__ruby__, __metaclass__);
        IRubyObject ruby_arg_num = JavaUtil.convertJavaToRuby(__ruby__, num);
        Helpers.invoke(__ruby__.getCurrentContext(), this, "initialize", ruby_arg_num);

    }

}
