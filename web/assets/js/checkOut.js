

payhere.onCompleted = function onCompleted(orderId) {
    console.log("Payment completed. OrderID:" + orderId);

    // Show success alert
    Swal.fire({
        title: 'Success!',
        text: "Payment successful",
        icon: 'success',
        confirmButtonText: 'Cool'
    }).then((result) => {
        // If 'Cool' button is clicked, reload the page
        if (result.isConfirmed) {
            window.location.reload();  // Reload the page
        }
    });
};


// Payment window closed
payhere.onDismissed = function onDismissed() {
    console.log("Payment dismissed");


};

// Error occurred
payhere.onError = function onError(error) {

    console.log("Error:" + error);
};



async function loadData() {

    const response = await fetch(
            "LoadCheckOut"
            );

    if (response.ok) {

        const json = await response.json();
        console.log(json);

        if (json.success) {

            const address = json.address;
            const cityList = json.cityList;
            const cartList = json.cartList;


//              //load citis
            // Populate city list
            let citySelect = document.getElementById("city");
            citySelect.length = 1; // Keep the "Select City" option

// Clear existing dynamically added options before adding new ones
            while (citySelect.options.length > 1) {
                citySelect.remove(1);
            }

            cityList.forEach(city => {
                let cityOption = document.createElement("option");
                cityOption.innerHTML = city.name;
                cityOption.value = city.id.toString(); // Ensure the value is a string
                citySelect.appendChild(cityOption);
            });

// When the checkbox is checked, load the address and set city
            let currentAddressCheckbox = document.getElementById("checkbox1");
            currentAddressCheckbox.addEventListener("change", e => {
                let fname = document.getElementById("first-name");
                let lname = document.getElementById("last-name");
                let line1 = document.getElementById("address1");
                let line2 = document.getElementById("address2");
                let postalCode = document.getElementById("postal-code");
                let mobile = document.getElementById("mobile");

                if (currentAddressCheckbox.checked) {
                    fname.value = address.first_name;
                    lname.value = address.last_name;

                    // Select the correct city by setting the value
                    let cityValue = address.city.id.toString(); // Ensure both are strings
                    citySelect.value = cityValue; // This should match if the types are consistent

                    // Disable the city selection
                    citySelect.disabled = true;
                    citySelect.dispatchEvent(new Event("change"));

                    line1.value = address.line1;
                    line2.value = address.line2;
                    postalCode.value = address.postal_code;
                    mobile.value = address.mobile;
                } else {
                    fname.value = "";
                    lname.value = "";
                    citySelect.value = "3"; // Set it back to "Select City" option (or whatever default value)
                    citySelect.disabled = false;

                    line1.value = "";
                    line2.value = "";
                    postalCode.value = "";
                    mobile.value = "";
                }
            });

            //load cart items
            let st_item = document.getElementById("st-item-tr");
            let st_subTotle = document.getElementById("st-subtotal-tr");
            let st_shipping_tr = document.getElementById("st-shipping-tr");
            let st_order_total_tr = document.getElementById("st-order-total-tr");
            let st_tbody = document.getElementById("st-tbody");

            st_tbody.innerHTML = "";

            let subTot = 0;
            let shipping = 0;

            cartList.forEach(item => {
                let stitemClone = st_item.cloneNode(true);
                stitemClone.querySelector("#st-item-title").innerHTML = item.product.title;
                stitemClone.querySelector("#st-item-qty").innerHTML = "x " + item.qty;
                stitemClone.querySelector("#st-item-img").src = "product_images/" + item.product.id + "/image1.png";
                stitemClone.querySelector("#st-item-subtotal").innerHTML = "Rs." + new Intl.NumberFormat("en-US", {
                    minimumFractionDigits: 2
                }).format(item.product.price * item.qty);

                subTot += item.product.price * item.qty;
                shipping += item.product.shipping;


                st_tbody.appendChild(stitemClone);
            });

            st_subTotle.querySelector("#st-subtotal").innerHTML = "Rs." + new Intl.NumberFormat("en-US", {
                minimumFractionDigits: 2
            }).format(subTot);
            st_tbody.appendChild(st_subTotle);

            //update total on change
            citySelect.addEventListener("change", e => {
                //update shipping chargers


                st_shipping_tr.querySelector("#st-shipping-amount").innerHTML = "Rs." + new Intl.NumberFormat("en-US", {
                    minimumFractionDigits: 2
                }).format(shipping);
                st_tbody.appendChild(st_shipping_tr);

                //update total
                st_order_total_tr.querySelector("#st-order-total-amount").innerHTML = "Rs." + new Intl.NumberFormat("en-US", {
                    minimumFractionDigits: 2
                }).format(subTot + shipping);
                st_tbody.appendChild(st_order_total_tr);

            });


            citySelect.dispatchEvent(new Event("change"));



        } else {
            window.location = "signin.html";
        }

    }
}

async  function checkout() {

    //check address status
    let isCurrentAddress = document.getElementById("checkbox1").checked;

    //get address data
    let fname = document.getElementById("first-name");
    let lname = document.getElementById("last-name");
    let line1 = document.getElementById("address1");
    let line2 = document.getElementById("address2");
    let postalCode = document.getElementById("postal-code");
    let mobile = document.getElementById("mobile");
    let citySelect = document.getElementById("city");

    const data = {
        isCurrentAddress: isCurrentAddress,
        fname: fname.value,
        lname: lname.value,
        line1: line1.value,
        line2: line2.value,
        postalCode: postalCode.value,
        mobile: mobile.value,
        city: citySelect.value

    };

    const response = await  fetch(
            "Checkout",
            {
                method: "POST",
                body: JSON.stringify(data),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );


    if (response.ok) {
        const json = await  response.json();

        if (json.success) {
            console.log(json.payhereJson);
            payhere.startPayment(json.payhereJson);
        } else {
            Swal.fire({
                title: 'Error!',
                text: json.msg,
                icon: 'error',
                confirmButtonText: 'Cool'
            })
        }

    } else {
        console.log("Error");
    }
}
//
// 
//

