async function loadWishItems() {

    const response = await fetch(
            "LoadWishItems"

            );

    if (response.ok) {

        const json = await response.json();
        
        console.log(json);


        if (json.length == 0) {
            Swal.fire({
                title: 'Error!',
                text: "Your wish list is empty",
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


//            document.getElementById("wish-item-title").innerHTML = json.product.title;

            let cartItemContainer = document.getElementById("wish-item-container");
            let cartItemRow = document.getElementById("wish-item-row");

// Clear the container before adding new items
            cartItemContainer.innerHTML = "";

            json.forEach(item => {
                console.log(item.product.title);

                // Clone the cart item row
                let cartItemRowClone = cartItemRow.cloneNode(true);
                cartItemRowClone.querySelector("#wish-item-a").href = "details.html?id=" + item.product.id;
                cartItemRowClone.querySelector("#wish-item-a2").href = "details.html?id=" + item.product.id;
                cartItemRowClone.querySelector("#wish-item-id").innerHTML = item.product.id;
                cartItemRowClone.querySelector("#wish-item-img").src = "product_images/" + item.product.id + "/image1.png";
                cartItemRowClone.querySelector("#wish-item-title").innerHTML = item.product.title;
                cartItemRowClone.querySelector("#wish-item-price").innerHTML = "Rs. " + new Intl.NumberFormat("en-US", {
                    minimumFractionDigits: 2
                }).format(item.product.price);
                cartItemRowClone.querySelector("#wish-item-shipping").innerHTML = "Rs. " + new Intl.NumberFormat("en-US", {
                    minimumFractionDigits: 2
                }).format(item.product.shipping);

                // Append the cloned row to the container
                cartItemContainer.appendChild(cartItemRowClone);




            });

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


async  function removefromWish() {


    const id = document.getElementById("wish-item-id").innerText;

    const response = await fetch(
            "removefromWish?id=" + id

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

