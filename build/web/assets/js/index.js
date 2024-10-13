async function checkSign() {


    const response = await fetch(
            "checkSignIn",
            );

    if (response.ok) {
        const json = await response.json();
//        console.log(json);
        const response_dto = json.response_dto;


        if (response_dto.success) {

            const user = response_dto.content;
//            console.log(user);

            let st_quick_link = document.getElementById("st-quick-link");

            st_quick_link.innerHTML = user.fisrt_name + " " + user.last_name;



            let logIn = document.getElementById("logIn");
            logIn.href = "#";
            logIn.innerHTML = "";

            let signUp = document.getElementById("signUp");
            signUp.href = "SignOut";
            signUp.innerHTML = "Sign Out";


        }



    } else {
        document.getElementById("massage").innerHTML = "Please try again later";
    }
}

async  function  loadhomepageproduct() {

    let ProductHtml = document.getElementById("product");
    document.getElementById("product-main").innerHTML = "";

    const response = await  fetch("LoadhomeProduct");
    if (response.ok) {
        const json = await response.json();
        
//        console.log( json.productList);

        json.productList.forEach(item => {

            let ProductCloneHtml = ProductHtml.cloneNode(true);

            ProductCloneHtml.querySelector("#product-image1").src = "product_images/" + item.id + "/image1.png";
            ProductCloneHtml.querySelector("#product-image2").src = "product_images/" + item.id + "/image2.png";
            ProductCloneHtml.querySelector("#product-a1").href = "details.html?id=" + item.id;
            ProductCloneHtml.querySelector("#product-a2").href = "details.html?id=" + item.id;
            ProductCloneHtml.querySelector("#product-a3").href = "details.html?id=" + item.id;
            ProductCloneHtml.querySelector("#product-title").innerHTML = item.title;

            ProductCloneHtml.querySelector("#product-price").innerHTML = "Rs." + new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format(item.price);


            ProductCloneHtml.querySelector("#add-to-cart-other").addEventListener(
                    "click", (e) => {
                addToCart(item.id, "1");
            });
    
    ProductCloneHtml.querySelector("#add-to-wish-other").addEventListener(
                    "click", (e) => {
                addToWish(item.id);
            });

            document.getElementById("product-main").appendChild(ProductCloneHtml);

        });
    }else{
        console.log("error");
        
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


async function addToWish(id) {



    const response = await fetch(
            "AddToWish?id=" + id 

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







