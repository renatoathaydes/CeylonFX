/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javafx.scene.chart;


import org.junit.Test;
import static org.junit.Assert.assertEquals;
import javafx.collections.*;

import com.sun.javafx.pgstub.StubToolkit;
import com.sun.javafx.tk.Toolkit;
import java.util.Arrays;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.layout.Region;

import org.junit.Ignore;

@Ignore
public class StackedBarChartTest extends XYChartTestBase {
    
    private Scene scene;
    private StubToolkit toolkit;
    private Stage stage;
    StackedBarChart<String,Number> sbc;
    final XYChart.Series<String,Number> series1 = new XYChart.Series<String,Number>();
    final XYChart.Series<String,Number> series2 = new XYChart.Series<String,Number>();
    final XYChart.Series<String,Number> series3 = new XYChart.Series<String,Number>();
    final String[] years = {"2007", "2008", "2009"};
    
    protected Chart createChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        sbc = new StackedBarChart<String,Number>(xAxis,yAxis);
        xAxis.setLabel("Year");
        xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(years)));
        yAxis.setLabel("Price");
        sbc.setTitle("HelloStackedBarChart");
        // Populate Chart data
        ObservableList<XYChart.Data> data = FXCollections.observableArrayList();
        series1.getData().add(new XYChart.Data<String,Number>(years[0], 567));
        series1.getData().add(new XYChart.Data<String,Number>(years[1], 1292));
        series1.getData().add(new XYChart.Data<String,Number>(years[2], 2180));
        series2.getData().add(new XYChart.Data<String,Number>(years[0], 956));
        series2.getData().add(new XYChart.Data<String,Number>(years[1], 1665));
        series2.getData().add(new XYChart.Data<String,Number>(years[2], 2450));
        series3.getData().add(new XYChart.Data<String,Number>(years[0], 800));
        series3.getData().add(new XYChart.Data<String,Number>(years[1], 1000));
        series3.getData().add(new XYChart.Data<String,Number>(years[2], 2800));
        return sbc;
    }
    
    @Test
    public void testAddingAutoRangingCategoryAxis() {
        CategoryAxis xAxis = new CategoryAxis();
        String[] years = {"2007", "2008", "2009"};
        NumberAxis yAxis = new NumberAxis();
        ObservableList<StackedBarChart.Series> barChartData = FXCollections.observableArrayList(
            new StackedBarChart.Series("Region 1", FXCollections.observableArrayList(
               new StackedBarChart.Data(years[0], 567d),
               new StackedBarChart.Data(years[1], 1292d),
               new StackedBarChart.Data(years[2], 1292d)
            )),
            new StackedBarChart.Series("Region 2", FXCollections.observableArrayList(
               new StackedBarChart.Data(years[0], 956),
               new StackedBarChart.Data(years[1], 1665),
               new StackedBarChart.Data(years[2], 2559)
            ))
        );
        StackedBarChart sbc = new StackedBarChart(xAxis, yAxis, barChartData, 25.0d);
        scene = new Scene(sbc,800,600);
        stage = new Stage();
        stage.setScene(scene);
        stage.show();
        pulse();
         assertEquals(6, sbc.getPlotChildren().size());
    }
    
    @Test
    public void testSeriesAdd() {
        startApp();
        sbc.getData().addAll(series1, series2, series3);
        pulse();
        ObservableList<Node> childrenList = sbc.getPlotChildren();
        StringBuffer sb = new StringBuffer();
        assertEquals(9, childrenList.size());
        for (Node n : childrenList) {
            assertEquals("chart-bar", n.getStyleClass().get(0));
        }
        // compute bounds for the first series
        sb = computeBoundsString((Region)childrenList.get(0), (Region)childrenList.get(1),
                (Region)childrenList.get(2));
        assertEquals("10 479 234 36 254 432 234 83 499 375 234 140 ", sb.toString());
        
        // compute bounds for the second series
//        sb = computeBoundsString((Region)childrenList.get(3), (Region)childrenList.get(4),
//                (Region)childrenList.get(5));
//        assertEquals("10 421 236 62 256 328 236 108 501 220 236 158 ", sb.toString());
//        
//        // compute bounds for the third series
//        sb = computeBoundsString((Region)childrenList.get(6), (Region)childrenList.get(7),
//                (Region)childrenList.get(8));
//        assertEquals("10 370 236 51 256 264 236 64 501 39 236 181 ", sb.toString());
    }
    
    @Test
    public void testSeriesRemove() {
        startApp();
        sbc.getData().addAll(series1);
        pulse();
        assertEquals(3, sbc.getPlotChildren().size());
        if (!sbc.getData().isEmpty()) {
            sbc.getData().remove(0);
            pulse();
            assertEquals(0, sbc.getPlotChildren().size());
        }
    }
    
    @Test
    public void testDataItemAdd() {
        startApp();
        sbc.getData().addAll(series1);
        pulse();
        series1.getData().add(new XYChart.Data<String, Number>("2010", 750));
        pulse();
        assertEquals(4, sbc.getPlotChildren().size());
    }
    
    @Test
    public void testDataItemInsert() {
        startApp();
        sbc.getData().addAll(series1);
        pulse();
        series1.getData().add(1, new XYChart.Data<String, Number>("2010", 750));
        pulse();
        assertEquals(4, sbc.getPlotChildren().size());
    }
     
    @Test
    public void testDataItemRemove() {
        startApp();
        sbc.getData().addAll(series1);
        pulse();
        if (!sbc.getData().isEmpty()) {
            series1.getData().remove(0);
            pulse();
            assertEquals(2, sbc.getPlotChildren().size());
        }
    }
    
    @Test @Ignore
    public void testDataItemChange() {
        startApp();
        sbc.getData().addAll(series1);
        pulse();
        if (!sbc.getData().isEmpty()) {
            StringBuffer sb = new StringBuffer();
            ObservableList<Node> childrenList = sbc.getPlotChildren();
            // compute bounds for the first series before data change
            sb = computeBoundsString((Region)childrenList.get(0), (Region)childrenList.get(1),
                (Region)childrenList.get(2));
            assertEquals("10 380 216 127 236 216 216 291 461 16 216 491 ", sb.toString());
            
            XYChart.Data<String, Number> d = series1.getData().get(2);
            d.setYValue(500d);
            pulse();
            
            // compute bounds for the first series after data change
            sb = computeBoundsString((Region)childrenList.get(0), (Region)childrenList.get(1),
                (Region)childrenList.get(2));
            assertEquals("10 302 216 205 236 40 216 467 461 326 216 181 ", sb.toString());
        }
    }
}
