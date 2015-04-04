package org.mixer2.sample.training1.view;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.apache.catalina.core.ApplicationContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.training1.Application;
import org.mixer2.sample.training1.bean.Bill;
import org.mixer2.sample.training1.controller.BillController;
import org.mixer2.sample.training1.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
public class M2billViewTest {

    @Autowired
    protected Mixer2Engine mixer2Engine;

    @Autowired
    protected ResourceLoader resourceLoader;

    @Autowired
    protected BillService billService;

    @Test
    public void test再発行ではない() throws Exception {

    }

    @Test
    public void test再発行() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        InputStream is = resourceLoader.getResource(
                "classpath:/templates/m2bill.html").getInputStream();
        Html tmplHtml = mixer2Engine.loadHtmlTemplate(is);

        Model model = new ExtendedModelMap();
        Bill bill = billService.createBill();
        model.addAttribute("bill", bill);

        M2billView m2billView = new M2billView();
        Html html = m2billView.renderHtml(tmplHtml, model.asMap(), request,
                response);

        Assert.assertNotNull(html.getById("isReissue"));

    }

}