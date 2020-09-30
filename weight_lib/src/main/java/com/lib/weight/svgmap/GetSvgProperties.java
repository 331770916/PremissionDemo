package com.lib.weight.svgmap;

import android.content.Context;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.annotation.RawRes;

/**
 * 获取svg地图中的属性值
 */
public class GetSvgProperties {

    public static List<Area> getMapSvgProperties(Context context, @RawRes int res) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Area> areas = new ArrayList<>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = context.getResources().openRawResource(res);
            Document document = builder.parse(is);
            NodeList svgNodeList = document.getElementsByTagName("path");
            for (int i = 0; i < svgNodeList.getLength(); i++) {
                Element element = (Element) svgNodeList.item(i);
                String path = element.getAttribute("android:pathData");
                String colorValue = element.getAttribute("android:fillColor");
                String nameValue = element.getAttribute("android:name");

                Area area = new Area(context);
                Path path_svg = PathParser.createPathFromPathData(path);
                area.setPath(path_svg);
                area.setColorValue(colorValue);
                area.setNameValue(nameValue);


                RectF rectF = new RectF();
                path_svg.computeBounds(rectF, true);
                Region region = new Region();
                region.setPath(path_svg, new Region((int) (rectF.left), (int) (rectF.top), (int) (rectF.right), (int) (rectF.bottom)));
                area.setRegion(region);
                areas.add(area);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return areas;
    }

}
