console.log("This is script file");

const toggleSidebar = () => {
    if($('.sidebar').is(":visible")){
        // true
        // we need to close this
        $('.sidebar').css('display', 'none');
        $('.content').css('margin-left', '0%');
    } else {
        // false
        // we need to open this
        $('.sidebar').css('display', 'block');
        $('.content').css('margin-left', '20%');
    }
}

const search = () => {
    // console.log("searching...");  
    
    let query = $('#search-input').val();
    if(query == ''){
        $('.search-result').hide();
    } else {
        console.log(query);
        
        let url = `http://localhost:8080/search/${query}`;

        fetch(url)
            .then((response) => response.json())
            .then((data) => {
                console.log(data);
                let text = `<div class='list-group'>`;

                data.forEach((contact) => {
                    text += `<a href='/user/${contact.cId}/contact' class='list-group-item list-group-item-action'>${contact.name}</a>`;
                })

                text += `</div>`;
                
                $('.search-result').html(text);
                $('.search-result').show();
            })
            .catch((error) => {
                console.log(error);
            });
    }
}

