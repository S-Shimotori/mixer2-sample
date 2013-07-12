package org.mixer2.sample.web.view;

import java.util.List;

import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Input;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.jaxb.xhtml.Table;
import org.mixer2.jaxb.xhtml.Tbody;
import org.mixer2.jaxb.xhtml.Tr;
import org.mixer2.sample.web.dto.Cart;
import org.mixer2.sample.web.dto.CartItem;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;

public class CartHelper {

    /**
     * replace table on template by item data.
     * @param html cart view page template data
     * @param itemList
     * @throws TagTypeUnmatchException
     */
    public static void replaceCartForm(Html html, Cart cart) throws TagTypeUnmatchException {

        List<CartItem> itemList = cart.getReadOnlyItemList();

        // if cart is empty, remove cart form tag and return immediately.
        if (itemList.size() < 1) {
            html.getBody().removeById("cartForm");
            return;
        } else {
        	// if cart is not empty, remove empty cart message.
        	html.getBody().removeById("emptyCart");
        }

        // get contextPath
        String ctx = RequestUtil.getRequest().getContextPath();

        // keep copy of first tr tag
        Tbody cartTbody = html.getBody().getById("cartTable", Table.class)
                .getById("cartTbody", Tbody.class);
        Tr baseTr = cartTbody.getTr().get(0).copy(Tr.class);
        cartTbody.unsetTr(); // equals .getTr().clear()

        // replace attribute of form tag
        html.getBody().getById("cartForm", Form.class).setMethod("post");
        html.getBody().getById("cartForm", Form.class).setAction(
                ctx + "/cart/checkout");

        // embed tr tag by item data
        for (CartItem cartItem : itemList) {

            // create tr from copy
            Tr tr = baseTr.copy(Tr.class);

            // item name
            Span itemNameSpan = new Span();
            itemNameSpan.getContent().add(cartItem.getItem().getName());
            itemNameSpan.addCssClass("itemName");
            tr.replaceDescendants("itemName", Span.class, itemNameSpan);

            // item price
            Span itemPriceSpan = new Span();
            itemPriceSpan.getContent().add(
                    cartItem.getItem().getPrice().toString());
            itemPriceSpan.addCssClass("itemPrice");
            tr.replaceDescendants("itemPrice", Span.class, itemPriceSpan);

            // item amount
            Input input = tr.getDescendants("itemAmount", Input.class).get(0);
            input.setName("amountArray");
            input.setValue(Integer.toString(cartItem.getAmount()));

            // add tr tag to tbody tag.
            cartTbody.getTr().add(tr);
        }

    }
}
