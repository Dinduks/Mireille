package foo;

class A {
    public String foo = bar("hello");

    public void main (String [] args)
    {
        B b = new B();
        b.foo(1, 2.0F);
        bar("hello again");
    }

    public String bar(String baz) {
        this.toString();
        return baz;
    }
}

class B {
    public boolean foo(int i, float j) {
        return true;
    }
}