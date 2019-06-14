package org.tranphucbol.factory.method;

public class App {
    public static void main(String[] args) {
        ShapeFactory shapeFactory = new ShapeFactory();

        Shape rect = shapeFactory.createShape(ShapeFactory.RECTANGLE);
        Shape circle = shapeFactory.createShape(ShapeFactory.CIRCLE);
    }
}
