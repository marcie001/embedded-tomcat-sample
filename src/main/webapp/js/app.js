// 削除ボタン
Array.prototype.forEach.call(document.querySelectorAll('.delete'), (e) => {	
	e.addEventListener('click', (ev) => {
		ev.preventDefault();
		fetch(ev.target.dataset.href, { method: 'DELETE' })
		.then((response) => {
			if (response.ok) {
				ev.target.parentNode.parentNode.remove();
			} else {
				throw new Error("response status: " + response.status);
			}
		})
		.catch(error => console.log(error));
		return false;
	}, false);
});

Array.prototype.forEach.call(document.querySelectorAll('.update'), (e) => {	
	e.addEventListener('click', (ev) => {
		var e0 = ev.target.parentNode.parentNode;
		e0.style.display = "none";
		e0.nextElementSibling.style.display = "table-row";
	}, false);
});
