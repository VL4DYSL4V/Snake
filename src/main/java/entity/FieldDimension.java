package entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class FieldDimension implements Serializable {

    private int minX;
    private int maxX;

    private int minY;
    private int maxY;

    private int width;
    private int height;

    private static final long serialVersionUID = 5005034861975834645L;

    private FieldDimension(){}

    public FieldDimension(int width, int height) {
        this.width = width;
        this.height = height;
        this.minX = 0;
        this.maxX = width - 1;
        this.minY = 0;
        this.maxY = height - 1;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDimension that = (FieldDimension) o;
        return minX == that.minX &&
                maxX == that.maxX &&
                minY == that.minY &&
                maxY == that.maxY &&
                width == that.width &&
                height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, maxX, minY, maxY, width, height);
    }

    @Override
    public String toString() {
        return "FieldDimension{" +
                "minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
