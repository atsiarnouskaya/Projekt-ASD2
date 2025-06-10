public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        boolean proceed = true;
        menu.list();
        while (proceed) {
            proceed = menu.move();
        }
    }
}


