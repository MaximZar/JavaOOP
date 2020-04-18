import java.util.Scanner;
public class Lab2 {
    public static void main(String args[]) {
        //Ввод трех точек
        Scanner input = new Scanner(System.in);
        System.out.println("Введите координаты первой точки");
        System.out.print("Введите x: ");
        double x = input.nextDouble();
        System.out.print("Введите y: ");
        double y = input.nextDouble();
        System.out.print("Введите z: ");
        double z = input.nextDouble();
        Point3d firstPoint = new Point3d(x, y, z);

        System.out.println("Введите координаты второй точки");
        System.out.print("Введите x: ");
        x = input.nextDouble();
        System.out.print("Введите y: ");
        y = input.nextDouble();
        System.out.print("Введите z: ");
        z = input.nextDouble();
        Point3d secondPoint = new Point3d(x, y, z);

        System.out.println("Введите координаты третьей точки");
        System.out.print("Введите x: ");
        x = input.nextDouble();
        System.out.print("Введите y: ");
        y = input.nextDouble();
        System.out.print("Введите z: ");
        z = input.nextDouble();
        Point3d thirdPoint = new Point3d(x, y, z);

        // Проверка точек
        if (!(
                firstPoint.Comp(secondPoint.getX(), secondPoint.getY(), secondPoint.getZ()) &&
                firstPoint.Comp(thirdPoint.getX(), thirdPoint.getY(), thirdPoint.getZ()) &&
                secondPoint.Comp(thirdPoint.getX(), thirdPoint.getY(), thirdPoint.getZ())
           ))
        {
                System.out.println("Вы ввели неверные данные точек");
                return;
        }

        // Вычисление площади
        double S = computeArea(firstPoint, secondPoint, thirdPoint);
        System.out.println("Площадь треугольника: " + S);
    }
    public static double computeArea(Point3d firstPoint, Point3d secondPoint, Point3d thirdPoint) {
        /*Получаем длинну 1 стороны*/
        double x = Math.pow(secondPoint.getX() - firstPoint.getX(), 2);
        double y = Math.pow(secondPoint.getY() - firstPoint.getY(), 2);
        double z = Math.pow(secondPoint.getZ() - firstPoint.getZ(), 2);
        double a = Math.pow(x+y+z , 0.5);
        /*Получаем длинну 2 стороны*/
        x = Math.pow(thirdPoint.getX() - secondPoint.getX(), 2);
        y = Math.pow(thirdPoint.getY() - secondPoint.getY(), 2);
        z = Math.pow(thirdPoint.getZ() - secondPoint.getZ(), 2);
        double b = Math.pow(x+y+z , 0.5);
        /*Получаем длинну 3 стороны*/
        x = Math.pow(thirdPoint.getX() - firstPoint.getX(), 2);
        y = Math.pow(thirdPoint.getY() - firstPoint.getY(), 2);
        z = Math.pow(thirdPoint.getZ() - firstPoint.getZ(), 2);
        double c = Math.pow(x+y+z , 0.5);
        /* Находим полупериметр */
        double p = (a+b+c)/2;
        /* Находим площадь по формуле Герона */
        double S = Math.pow(p*(p-a)*(p-b)*(p-c), 0.5);
        return S;
    }
}
