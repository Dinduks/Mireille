package foo;

class TestOriginal {
    public static void main(String[] args) {
        Greeter greeter = new Greeter();

        int[] ints = new int[42];
        int[] foo = ints.clone();

        greeter.inEnglish("Samy");
        greeter.toString();
    }
}

class Greeter {
    public String inEnglish(String name) {
        return "Hello " + name + "!";
    }
}
