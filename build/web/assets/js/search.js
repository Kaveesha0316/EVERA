
async  function loadData() {


    const response = await  fetch("LoadData");

    if (response.ok) {
        const json = await  response.json();
        console.log(json);

        //load maincategory list
        loadOption("maincategory", json.mainCategoryList);
        //load subcategory list
        loadOption("subcategory", json.subCategoryList);
//        //load brand list
        loadOption("brand", json.brandList);
//        //load size list
        loadOption("size", json.SizesList);
//
//
        updateProductView(json);


    } else {
        console.log("error");
    }

}

function loadOption(prefix, dataList) {
    let Options = document.getElementById(prefix + "-select");
//    let Li = document.getElementById(prefix + "-li");
//    Options.innerHTML = "";

    let categoryList = dataList;
    categoryList.forEach(data => {

        let optionTag = document.createElement("option");
        optionTag.innerHTML = data.name;
        Options.appendChild(optionTag);

    });


}

async function searchProducts(firstResult) {

    //get search data
    let maincategory_name = document.getElementById("maincategory-select").value;
    let subcategory_name = document.getElementById("subcategory-select").value;
    let brand_name = document.getElementById("brand-select").value;
    let size_text = document.getElementById("size-select").value;
    let sort = document.getElementById("sort").value;
    let price_range_start = document.getElementById("priceStart").value;
    let price_range_end = document.getElementById("priceEnd").value;

    if (price_range_start < "0") {
        Swal.fire({
            title: 'Error!',
            text: "price cannot be minus",
            icon: 'error',
            confirmButtonText: 'Cool'
        })
    } else

    if (price_range_end < "0") {
        Swal.fire({
            title: 'Error!',
            text: "price cannot be minus",
            icon: 'error',
            confirmButtonText: 'Cool'
        })
    } else {

        const data = {
            maincategory_name: maincategory_name,
            subcategory_name: subcategory_name,
            brand_name: brand_name,
            size_name: size_text,
            sort_text: sort,
            price_range_start: price_range_start,
            price_range_end: price_range_end,
            firstResult: firstResult
        };



        const response = await  fetch(
                "SearchProducts",
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
            console.log(json);

            if (json.success) {

                updateProductView(json);

            }

        } else {
            console.log("error");
        }
    }
}

var st_product = document.getElementById("st-product");

var currentPage = 0;

function updateProductView(json) {
    //load product list
    let st_product_container = document.getElementById("st-product-container");
    st_product_container.innerHTML = "";

    json.productList.forEach(product => {
        let st_product_clone = st_product.cloneNode(true);

        st_product_clone.querySelector("#st-product-img").src = "product_images/" + product.id + "/image1.png";
        st_product_clone.querySelector("#st-product-img2").src = "product_images/" + product.id + "/image2.png";
        st_product_clone.querySelector("#st-product-price").innerHTML = "Rs." + new Intl.NumberFormat("en-US").format(product.price) + ".00";
        st_product_clone.querySelector("#st-product-title").innerHTML = product.title;
        st_product_clone.querySelector("#similer-product-a1").href = "details.html?id=" + product.id;
        st_product_clone.querySelector("#similer-product-a2").href = "details.html?id=" + product.id;

        st_product_clone.querySelector("#add-to-cart-other").addEventListener(
                "click", (e) => {

            addToCart(product.id, "1");
        }
        );

        st_product_container.appendChild(st_product_clone);
    
});
    //load paigination

    let paigination_container = document.getElementById("st-pagination-container");
    let paigination_btn = document.getElementById("st-pagination-btn");
    paigination_container.innerHTML = ""; // Clear the container on each update

    let pages = Math.ceil(json.allProductCount / 4); // Calculate total number of pages

// Add "Prev" button if not on the first page
    if (currentPage != 0) {
        let paigination_btn_clone_prev = paigination_btn.cloneNode(true);
        paigination_btn_clone_prev.innerHTML = "Prev";
        paigination_btn_clone_prev.classList.remove("active"); // Remove active class
        paigination_btn_clone_prev.addEventListener("click", e => {
            currentPage--;
            searchProducts(currentPage * 4);
        });
        paigination_container.appendChild(paigination_btn_clone_prev);
    }

// Add page number buttons
    for (let i = 0; i < pages; i++) {
        let paigination_btn_clone = paigination_btn.cloneNode(true);
        paigination_btn_clone.innerHTML = i + 1;

        // Mark current page as active
        if (currentPage === i) {
            paigination_btn_clone.classList.add("active");
        } else {
            paigination_btn_clone.classList.remove("active");
        }

        // Add click event for pagination navigation
        paigination_btn_clone.addEventListener("click", e => {
            currentPage = i;
            searchProducts(i * 4);
        });

        paigination_container.appendChild(paigination_btn_clone);
    }

// Add "Next" button if not on the last page
    if (currentPage != (pages - 1)) {
        let paigination_btn_clone_next = paigination_btn.cloneNode(true);
        paigination_btn_clone_next.innerHTML = "Next";
        paigination_btn_clone_next.classList.remove("active"); // Remove active class
        paigination_btn_clone_next.addEventListener("click", e => {
            currentPage++;
            searchProducts(currentPage * 4);
        });
        paigination_container.appendChild(paigination_btn_clone_next);
    }



}


async function addToCart(id, qty) {



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

function reload() {
    window.location.reload();
}