

var subCategoryList;
async  function loadFeatues() {
   

    const response = await  fetch(
            "LoadFeaturs"

            );

    if (response.ok) {
        const json = await  response.json();

        const mainCategoryList = json.mainCategoryList;
        subCategoryList = json.subCategoryList;
        const SizesList = json.SizesList;
        const brandList = json.brandList;
     


        loadSelect(mainCategoryList, "main-category-select", ["id", "name"]);
        loadSelect(subCategoryList, "sub-category-select", ["id", "name"]);  
        loadSelect(SizesList, "size-select", ["id", "name"]);
        loadSelect(brandList, "brand-select", ["id", "name"]);



    } else {
        console.log("error");
    }


}

function loadSelect(list, elementId, propertyArray) {
    const element = document.getElementById(elementId);
    list.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item[propertyArray[0]];
        optionTag.innerHTML = item[propertyArray[1]];
        element.appendChild(optionTag);
    });
}

function updateModels() {
    let subCategorySelect = document.getElementById("sub-category-select");
    subCategorySelect.length = 1;

    let selectedMainCategoryId = document.getElementById("main-category-select").value;
    
    subCategoryList.forEach(subcategory => {
       
        if (subcategory.mainCategory.id == selectedMainCategoryId) {
            let optionTag = document.createElement("option");
            optionTag.value = subcategory.id;
            optionTag.innerHTML = subcategory.name;
            subCategorySelect.appendChild(optionTag);
           
        }
    });
}

async function productListing() {
    const maincategorySelect = document.getElementById("main-category-select");
    const subcategorySelect = document.getElementById("sub-category-select");
    const titleTag = document.getElementById("title");
    const descriptionTag = document.getElementById("description");
    const brandSelect = document.getElementById("brand-select");
    const sizeSelect = document.getElementById("size-select");
    const priceTag = document.getElementById("price");
    const qtyTag = document.getElementById("qty");
    const shippingTag = document.getElementById("shipping");
    const image1Tag = document.getElementById("image1");
    const image2Tag = document.getElementById("image2");
    const image3Tag = document.getElementById("image3");

    // Validate images
    const validImageTypes = ['image/jpeg', 'image/png', 'image/gif'];

    // Function to validate image file
    function validateImage(file) {
        if (file && !validImageTypes.includes(file.type)) {
            return false;
        }
        return true;
    }

    // Check image 1
    if (image1Tag.files[0] && !validateImage(image1Tag.files[0])) {
        Swal.fire({
            title: 'Error!',
            text: 'Image 1 must be a valid image file (jpg, png, gif)',
            icon: 'error',
            confirmButtonText: 'OK'
        });
        return; // Stop form submission if validation fails
    }

    // Check image 2
    if (image2Tag.files[0] && !validateImage(image2Tag.files[0])) {
        Swal.fire({
            title: 'Error!',
            text: 'Image 2 must be a valid image file (jpg, png, gif)',
            icon: 'error',
            confirmButtonText: 'OK'
        });
        return; // Stop form submission if validation fails
    }

    // Check image 3
    if (image3Tag.files[0] && !validateImage(image3Tag.files[0])) {
        Swal.fire({
            title: 'Error!',
            text: 'Image 3 must be a valid image file (jpg, png, gif)',
            icon: 'error',
            confirmButtonText: 'OK'
        });
        return; // Stop form submission if validation fails
    }

    // Proceed if all images are valid
    const data = new FormData();
    data.append("maincategoryId", maincategorySelect.value);
    data.append("subcategorylId", subcategorySelect.value);
    data.append("title", titleTag.value);
    data.append("description", descriptionTag.value);
    data.append("brandId", brandSelect.value);
    data.append("sizeId", sizeSelect.value);
    data.append("price", priceTag.value);
    data.append("qtyId", qtyTag.value);
    data.append("shippingId", shippingTag.value);
    data.append("image1", image1Tag.files[0]);
    data.append("image2", image2Tag.files[0]);
    data.append("image3", image3Tag.files[0]);

    // Fetch API call
    const response = await fetch("AddProduct", {
        method: "POST",
        body: data
    });

    if (response.ok) {
        const json = await response.json();

        if (json.success) {
            // Reset form after successful submission
            maincategorySelect.value = 0;
            subcategorySelect.value = 0;
            brandSelect.value = 0;
            sizeSelect.value = 0;
            titleTag.value = "";
            descriptionTag.value = "";
            shippingTag.value = "";
            priceTag.value = "";
            qtyTag.value = "";
            image1Tag.value = null;
            image2Tag.value = null;
            image3Tag.value = null;

            Swal.fire({
                title: 'Success!',
                text: json.content,
                icon: 'success',
                confirmButtonText: 'Cool'
            });
        } else {
            Swal.fire({
                title: 'Error!',
                text: json.content,
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    } else {
        console.log("Error submitting form");
    }
}


