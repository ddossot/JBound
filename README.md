<pre>
             _        _               _      _                  _             _         
            /\ \     / /\            /\ \   /\_\               /\ \     _    /\ \       
            \ \ \   / /  \          /  \ \ / / /         _    /  \ \   /\_\ /  \ \____  
            /\ \_\ / / /\ \        / /\ \ \\ \ \__      /\_\ / /\ \ \_/ / // /\ \_____\ 
           / /\/_// / /\ \ \      / / /\ \ \\ \___\    / / // / /\ \___/ // / /\/___  / 
  _       / / /  / / /\ \_\ \    / / /  \ \_\\__  /   / / // / /  \/____// / /   / / /  
 /\ \    / / /  / / /\ \ \___\  / / /   / / // / /   / / // / /    / / // / /   / / /   
 \ \_\  / / /  / / /  \ \ \__/ / / /   / / // / /   / / // / /    / / // / /   / / /    
 / / /_/ / /  / / /____\_\ \  / / /___/ / // / /___/ / // / /    / / / \ \ \__/ / /     
/ / /__\/ /  / / /__________\/ / /____\/ // / /____\/ // / /    / / /   \ \___\/ /      
\/_______/   \/_____________/\/_________/ \/_________/ \/_/     \/_/     \/_____/       

</pre>

JBound is a simple utility that performs boundary checks on domain model objects. It injects extreme values (minimum, maximum, null) in constructor arguments and setters. It also calls getters, `equals`, `hashCode` and `toString`.

JBound ignores the actual outcome of these calls and only focuses on generic exceptions being thrown. If your application throws exceptions for bad data input with an explanation message, the exception will be considered to be purposeful. If the exception thrown has no message, it will be considered generic and an AssertionError will be thrown.

Here is code sample for a JUnit4 test that exercises a few beans:

    @Test
    public void fullyExerciseBeans() {
        JBound.run(new Exercises() {
            {
                forClasses(MutableBean.class, ImmutableBean.class);
            }
        });
    }

It is possible to accept generic exceptions from particular methods or constructors by specifying their full text signatures. It is of course a better idea to strengthen these methods, but it is not always possible. Here is a test that accepts generic exceptions from the class constructor:

    @Test
    public void exerciseBeanWithSkipping() {
        JBound.run(new Exercises() {
            {
                forClass(DefensiveBeanWithWeakConstructor.class).acceptingGenericExceptionsFrom(
                        "public org.jbound.subject.DefensiveBeanWithWeakConstructor(java.lang.String,int,java.lang.Integer,java.util.List)");
            }
        });
    } 

Do not be scared by the long String: you can copy/paste it from JBound assertion errors when they occur. Here is the first line of such an error:

java.lang.AssertionError: Received a generic class java.lang.NullPointerException when calling: public org.jbound.subject.DefensiveBeanWithWeakConstructor(java.lang.String,int,java.lang.Integer,java.util.List)

JBound will never accept Exceptions from equals, hashcode or toString. If you are unhappy with that, you can skip these tests for a particular class, as show here:

    @Test
    public void exerciseBeanWithSkipping() {
        JBound.run(new Exercises() {
            {
                forClass(FragileBean.class).skipping(EXERCISE.EQUALS, EXERCISE.HASHCODE, EXERCISE.TO_STRING);
            }
        });
    } 

If need be, you can register data samples for custom types using this simple syntax:

    @BeforeClass
    public static void registerCustomDataType() {
        JBound.registerCustomDataType(MyCustomType.class, null,
                new MyCustomType(), any other boundary value...);
    }

Enjoy JBound! Please report any issue. Pull requests are welcome :)

**Copyright © 2008-2015 David Dossot - MIT License**
