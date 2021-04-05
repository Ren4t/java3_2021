package homework1.fruitbox;

public class MainClass {
    public static void main(String[] args) {
        Box<Apple> appleBox = new Box<>();
        Box<Orange> orangeBox = new Box<>();
        Box<Orange> orangeBox2 = new Box<>();

        for (int i = 0; i < 9; i++) {
            appleBox.add(new Apple());
        }

        for (int i = 0; i < 6; i++) {
            orangeBox.add(new Orange());
        }

        System.out.println(appleBox.getWeight());
        System.out.println(orangeBox.getWeight());
        System.out.println(appleBox.compare(orangeBox));

        orangeBox2.overflow(orangeBox);
        System.out.println(orangeBox2.getBoxList());
    }
}
