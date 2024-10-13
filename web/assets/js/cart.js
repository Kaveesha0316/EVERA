async function loadCartItems() {

    const response = await fetch(
            "LoadCartItems"

            );

    if (response.ok) {

        const json = await response.json();

        if (json.length == 0) {
            Swal.fire({
                title: 'Error!',
                text: "Your cart is empty",
                icon: 'error',
                confirmButtonText: 'Cool'
            }).then((result) => {
                // If 'Cool' button is clicked, reload the page
                if (result.isConfirmed) {
                    window.location = "index.html"// Reload the page
                }
            });
            //            window.location = "index.html";

        } else {



//            document.getElementById("cart-item-title").innerHTML = json.product.title;

            let cartItemContainer = document.getElementById("cart-item-container");
            let cartItemRow = document.getElementById("cart-item-row");

            cartItemContainer.innerHTML = "";

            let totalQty = 0;
            let total = 0;
            let totalShipping = 0;
            let cartSubTotal = 0;

            json.forEach(item => {

                let itemSubtotal = (item.product.price * item.qty);

                total += itemSubtotal + item.product.shipping;
                totalQty += item.qty;
                totalShipping += item.product.shipping;
                cartSubTotal += itemSubtotal;


                let cartItemRowClone = cartItemRow.cloneNode(true);
                cartItemRowClone.querySelector("#cart-item-a").href = "details.html?id=" + item.product.id;
                cartItemRowClone.querySelector("#cart-item-id").innerHTML = item.product.id;
                cartItemRowClone.querySelector("#cart-item-img").src = "product_images/" + item.product.id + "/image1.png";
                cartItemRowClone.querySelector("#cart-item-title").innerHTML = item.product.title;
                cartItemRowClone.querySelector("#cart-item-price").innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US", {
                            minimumFractionDigits: 2
                        }).format(item.product.price);
                cartItemRowClone.querySelector("#cart-item-shipping").innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US", {
                            minimumFractionDigits: 2
                        }).format(item.product.shipping);
                cartItemRowClone.querySelector("#cart-item-qty").value = item.qty;
                cartItemRowClone.querySelector("#cart-item-subtotal").innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US", {
                            minimumFractionDigits: 2
                        }).format((itemSubtotal));
                cartItemContainer.appendChild(cartItemRowClone);


            });
            document.getElementById("cart-total-qty").innerHTML = totalQty;
            document.getElementById("cart-item-total").innerHTML = "Rs. " + new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format((cartSubTotal));
            document.getElementById("cart-shipping-total").innerHTML = "Rs. " + new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format((totalShipping));
            document.getElementById("cart-total").innerHTML = "Rs. " + new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format((total));
        }



    } else {
        Swal.fire({
            title: 'Error!',
            text: "Request faild",
            icon: 'error',
            confirmButtonText: 'Cool'
        })
    }

}


async  function removefromCart() {

    const id = document.getElementById("cart-item-id").innerText;

    const response = await fetch(
            "removefromCart?id=" + id

            );

    if (response.ok) {

        const json = await response.json();

        if (json.success) {
            Swal.fire({
                title: 'Success!',
                text: json.content,
                icon: 'success',
                confirmButtonText: 'Cool'
            }).then((result) => {
                // If 'Cool' button is clicked, reload the page
                if (result.isConfirmed) {
                    window.location.reload();// Reload the page
                }
            });

        } else {
            Swal.fire({
                title: 'Error!',
                text: json.content,
                icon: 'error',
                confirmButtonText: 'Cool'
            })
        }

    } else {

    }
}

