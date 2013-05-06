class DummyClass {
    public static void main(String[] args) {
        new DummyClass();
    }

    public DummyClass() {
        this.method1();
        this.method2("fooString");
        System.out.println("It works!");
    }

    public String method1() {
        return "";
    }

    protected int[] method2(String bar) {
        bar.toString();
        return new int[10];
    }
}