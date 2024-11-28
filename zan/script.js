
$(function(){
    const grid = document.getElementById('grid');
    const gridData = [];
    
    // 그리드 생성
    for (let row = 0; row < 6; row++) {
      for (let col = 0; col < 8; col++) {
        const cell = document.createElement('div');
        cell.classList.add('grid-item');
        cell.setAttribute('data-row', row);
        cell.setAttribute('data-col', col);
    
        // 드래그 가능한 요소 추가
        cell.addEventListener('click', () => {
          const input = prompt('이름을 입력하세요:');
          if (input) {
            cell.textContent = input;
            gridData.push({ row, col, name: input });
          }
        });
    
        grid.appendChild(cell);
      }
    }
    
    // JSON 저장
    document.getElementById('save').addEventListener('click', () => {
      const json = JSON.stringify(gridData, null, 2);
      const blob = new Blob([json], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'grid-data.json';
      a.click();
    });
    
    // JSON 불러오기
    document.getElementById('load').addEventListener('click', () => {
      const fileInput = document.getElementById('file-input');
      fileInput.click();
    
      fileInput.addEventListener('change', (event) => {
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.onload = () => {
          const data = JSON.parse(reader.result);
          data.forEach(({ row, col, name }) => {
            const cell = document.querySelector(
              `.grid-item[data-row="${row}"][data-col="${col}"]`
            );
            cell.textContent = name;
          });
        };
        reader.readAsText(file);
      });
    });
})

