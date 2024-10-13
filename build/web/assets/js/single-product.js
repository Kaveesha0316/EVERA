async  function loadProduct() {

    const parameters = new URLSearchParams(window.location.search);

    if (parameters.has("id")) {
        const productId = parameters.get("id");

        const response = await  fetch("LoadSingleProduct?id=" + productId);
        if (response.ok) {
            const json = await response.json();
            console.log(json);
            const id = json.product.id;
//            console.log(json.product.id);
//            console.log(json.productList);

            document.getElementById("image11").src = "product_images/" + id + "/image1.png";
            document.getElementById("image1").src = "product_images/" + id + "/image1.png";
            document.getElementById("image2").src = "product_images/" + id + "/image2.png";
            document.getElementById("image3").src = "product_images/" + id + "/image3.png";


            document.getElementById("product-title").innerHTML = json.product.title;
//            document.getElementById("product-publish-date").innerHTML = json.product.date_time;

            let price = json.product.price;
            let realPrice = (price * 10 / 100) + price;
            document.getElementById("product-price").innerHTML = "Rs." + new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format(price);

            document.getElementById("product-real-price").innerHTML = "Rs." + new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format(realPrice);
            document.getElementById("product-sub-category").innerHTML = json.product.subCategory.name;
            document.getElementById("product-category").innerHTML = json.product.subCategory.mainCategory.name;
            document.getElementById("product-brand").innerHTML = json.product.brand.name;
            document.getElementById("product-qty").innerHTML = json.product.qty;
            document.getElementById("product-shipping").innerHTML = "Rs." + new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format(json.product.shipping);

            document.getElementById("product-description").innerHTML = json.product.description;

            if (json.product.sizes.name == "S") {
                document.getElementById("product-id1").classList.add("size-active");
            } else if (json.product.sizes.name == "M") {
                document.getElementById("product-id2").classList.add("size-active");
            } else if (json.product.sizes.name == "L") {
                document.getElementById("product-id3").classList.add("size-active");
            } else if (json.product.sizes.name == "XL") {
                document.getElementById("product-id4").classList.add("size-active");
            } else if (json.product.sizes.name == "XXL") {
                document.getElementById("product-id5").classList.add("size-active");
            }

            if (json.product.qty == "0") {
                let addToCartBtn = document.getElementById("add-to-cart-main");

                // Disable the button by adding a class or style
                addToCartBtn.classList.add("disabled"); // Optionally, use a class to style it as disabled
                addToCartBtn.style.pointerEvents = "none"; // Prevent clicking
                addToCartBtn.style.opacity = "0.5"; // Dim the button visually
                addToCartBtn.innerHTML = "Out of Stock"; // Update the text if needed
            }


//add to cart main product////////
            document.getElementById("add-to-cart-main").addEventListener(
                    "click", (e) => {
                let qty = document.getElementById("add-to-cart-qty").value;
                if (qty < 1) {
                    Swal.fire({
                        title: 'Error!',
                        text: "Quentity can't be minus",
                        icon: 'error',
                        confirmButtonText: 'Cool'
                    })
                } else {
                     addToCart(json.product.id, qty);
                   
                }

            }
            );

            //similer product clone

            let ProductHtml = document.getElementById("similer-product");
            document.getElementById("similer-product-main").innerHTML = "";

            json.productList.forEach(item => {


                let ProductCloneHtml = ProductHtml.cloneNode(true);

                ProductCloneHtml.querySelector("#similer-product-image1").src = "product_images/" + item.id + "/image1.png";
                ProductCloneHtml.querySelector("#similer-product-image2").src = "product_images/" + item.id + "/image2.png";
                ProductCloneHtml.querySelector("#similer-product-a1").href = "details.html?id=" + item.id;
                ProductCloneHtml.querySelector("#similer-product-a2").href = "details.html?id=" + item.id;
                ProductCloneHtml.querySelector("#similer-product-a3").href = "details.html?id=" + item.id;
                ProductCloneHtml.querySelector("#similer-product-title").innerHTML = item.title;

                ProductCloneHtml.querySelector("#similer-product-price").innerHTML = "Rs." + new Intl.NumberFormat(
                        "en-US", {
                            minimumFractionDigits: 2
                        }).format(item.price);




                ProductCloneHtml.querySelector("#add-to-cart-other").addEventListener(
                        "click", (e) => {
                    addToCart(item.id, "1");
                }
                );

                document.getElementById("similer-product-main").appendChild(ProductCloneHtml);


            });

//           


        } else {
//            window.location = "index.html"

        }

    } else {
//        window.location = "index.html"
    }
}

async  function addToCart(id, qty) {



    const response = await fetch(
            "AddToCart?id=" + id + "&qty=" + qty

            );

    if (response.ok) {

        const json = await response.json();

        if (json.success) {
            Swal.fire({
                title: 'Success!',
                text: json.content,
                icon: 'succses',
                confirmButtonText: 'Cool'
            })
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
