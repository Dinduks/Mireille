class Test {

    public static void main(String[] args) {
        Greeter greeter = new Greeter();
        System.out.println(greeter.inEnglish("Samy"));
    }

}

class Greeter {
    public String inEnglish(String name) {
        return "Hello " + name + "!";
    }
}
