async function verifyAccount() {
      
   
    const dto = {

        verification: document.getElementById("verification").value,
    

    };

    const response = await fetch(
            "Verification",
            {
                method: "POST",
                body: JSON.stringify(dto),
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {

        const json = await response.json();
        if (json.success) {
         
            window.location = "signin.html";
        } else {
            document.getElementById("massage").innerHTML = json.content;
        }

    } else {
        document.getElementById("massage").innerHTML = "Please try again later";
    }
}