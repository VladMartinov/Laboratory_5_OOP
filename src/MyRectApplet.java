import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyRectApplet extends Applet implements ActionListener, MouseListener, MouseMotionListener {

    private List<Rectangle> rectangles;
    private Button rectButton, coloredRectButton, drawableRectButton;
    private Button saveButton, loadButton; // Новые кнопки
    private Rectangle selectedRect = null;
    private int mouseOffsetX, mouseOffsetY;

    private static final String SAVE_FILE = "rectangles.ser";

    public void init() {
        rectangles = new ArrayList<>();

        // Создание кнопок
        rectButton = new Button("Rectangle");
        coloredRectButton = new Button("ColoredRect");
        drawableRectButton = new Button("DrawableRect");
        saveButton = new Button("Save to File"); // Кнопка сохранения
        loadButton = new Button("Load from File"); // Кнопка загрузки

        // Добавление кнопок на апплет
        add(rectButton);
        add(coloredRectButton);
        add(drawableRectButton);
        add(saveButton); // Добавляем кнопку сохранения
        add(loadButton); // Добавляем кнопку загрузки

        // Установка слушателей событий для кнопок
        rectButton.addActionListener(this);
        coloredRectButton.addActionListener(this);
        drawableRectButton.addActionListener(this);
        saveButton.addActionListener(this); // Слушатель для кнопки сохранения
        loadButton.addActionListener(this); // Слушатель для кнопки загрузки

        // Установка слушателей событий для мыши
        addMouseListener(this);
        addMouseMotionListener(this);

    }

    // Обработчик нажатий кнопок
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rectButton) {
            rectangles.add(new Rectangle(50, 50, 100, 100));
        } else if (e.getSource() == coloredRectButton) {
            rectangles.add(new ColoredRect(50, 50, 100, 100, Color.BLACK, Color.GREEN));
        } else if (e.getSource() == drawableRectButton) {
            rectangles.add(new DrawableRect(50, 50, 100, 100, Color.BLUE));
        } else if (e.getSource() == saveButton) {
            saveRectanglesToFile();
        } else if (e.getSource() == loadButton) {
            loadRectanglesFromFile();
        }

        repaint(); // Перерисовать апплет, чтобы отобразить изменения
    }

    // Метод отрисовки апплета
    public void paint(Graphics g) {
        for (Rectangle rect : rectangles) {
            if (rect instanceof ColoredRect) {
                ((ColoredRect) rect).draw(g);
            } else if (rect instanceof DrawableRect) {
                ((DrawableRect) rect).draw(g);
            } else {
                g.drawRect(rect.x1, rect.y1, rect.x2 - rect.x1, rect.y2 - rect.y1);
            }
        }
    }

    // Обработчики событий мыши
    @Override
    public void mouseClicked(MouseEvent e) {} // не используется
    @Override
    public void mouseEntered(MouseEvent e) {} // не используется
    @Override
    public void mouseExited(MouseEvent e) {} // не используется


    // Обработка нажатия кнопки мыши
    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Проверяем, какой прямоугольник был нажат
        for (Rectangle rect : rectangles) {
            if (mouseX >= rect.x1 && mouseX <= rect.x2 && mouseY >= rect.y1 && mouseY <= rect.y2) {
                selectedRect = rect;
                mouseOffsetX = mouseX - rect.x1;
                mouseOffsetY = mouseY - rect.y1;
                break;
            }
        }
    }


    // Обработка отпускания кнопки мыши
    @Override
    public void mouseReleased(MouseEvent e) {
        selectedRect = null;
    }

    // Обработка перемещения мыши
    @Override
    public void mouseDragged(MouseEvent e) {
        if(selectedRect != null)
        {
            selectedRect.move(e.getX() - selectedRect.x1 - mouseOffsetX, e.getY() - selectedRect.y1-mouseOffsetY);
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {} // не используется


    // Сохранение прямоугольников в файл
    private void saveRectanglesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(rectangles);
            System.out.println("Rectangles saved to " + SAVE_FILE);
        } catch (IOException ex) {
            System.err.println("Error saving rectangles: " + ex.getMessage());
        }
    }

    // Загрузка прямоугольников из файла
    private void loadRectanglesFromFile() {
        File file = new File(SAVE_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("No save file or file is empty.");
            return; // Выходим, если файла нет
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            Object loadedRectangles = ois.readObject();

            if(loadedRectangles instanceof List){
                List<?> loadedList = (List<?>)loadedRectangles;

                for (Object item : loadedList) {
                    if(item instanceof Rectangle){
                        rectangles.add((Rectangle)item);
                    }

                }

                System.out.println("Rectangles loaded from " + SAVE_FILE);
            }
            else{
                System.out.println("Incorrect data in file " + SAVE_FILE);
            }

        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error loading rectangles: " + ex.getMessage());
        }

        repaint();
    }
}