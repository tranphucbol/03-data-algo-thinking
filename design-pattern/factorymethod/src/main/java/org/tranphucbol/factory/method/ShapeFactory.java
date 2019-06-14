package org.tranphucbol.factory.method;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ShapeFactory {
    public static final int RECTANGLE = 1;
    public static final int CIRCLE = 2;

    public static final Logger LOGGER = LoggerFactory.getLogger(ShapeFactory.class);

    public Shape createShape(int type) {
        switch (type) {
            case RECTANGLE:
                LOGGER.info("Create Rectangle");
                return new Rectangle();
            case CIRCLE:
                LOGGER.info("Create Circle");
                return new Circle();
            default:
                LOGGER.error("This type does not exist!");
                return null;
        }
    }
}
