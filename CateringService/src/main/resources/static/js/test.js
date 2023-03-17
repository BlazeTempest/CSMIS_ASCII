const toggle_test = document.getElementById('toggleTest');

toggle_test.addEventListener('change', function() {
	if (toggle_test.checked) {
		// checkbox is checked, toggle data is true
		// save toggle data to database, localStorage, or wherever you need to store it
		console.log(">>>> true")
		fetch('/saveToggleData', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({checked: toggle_test.checked })
		})

			.then(response => {
				console.log('Toggle data saved successfully');
			})
			.catch(error => {
				console.error('Error saving toggle data', error);
			});

	} else {
		// checkbox is not checked, toggle data is false
		// save toggle data to database, localStorage, or wherever you need to store it
		console.log(">>> false ")
		fetch('/saveToggleData', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({ checked: false })
		})
			.then(response => {
				console.log('Toggle data saved successfully');
			})
			.catch(error => {
				console.error('Error saving toggle data', error);
			});
	}
});

// retrieve toggle data from database, localStorage, or wherever you stored it
const savedToggleData = false; // example value

// set checkbox state based on saved toggle data
toggle_test.checked = savedToggleData;
