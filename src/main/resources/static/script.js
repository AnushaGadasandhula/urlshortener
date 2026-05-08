
const API_BASE_URL = 'http://localhost:8080';

const longUrlInput = document.getElementById('longUrl');
const shortenBtn = document.getElementById('shortenBtn');
const resultDiv = document.getElementById('result');
const errorDiv = document.getElementById('error');
const shortLink = document.getElementById('shortLink');
const clickCount = document.getElementById('clickCount');
const longUrlDisplay = document.getElementById('longUrlDisplay');
const copyBtn = document.getElementById('copyBtn');


async function shortenUrl() {
    const longUrl = longUrlInput.value.trim();
    
    if (!longUrl) {
        showError('Please enter a URL');
        return;
    }
    
  
    resultDiv.classList.add('hidden');
    errorDiv.classList.add('hidden');
    

    shortenBtn.textContent = 'Shortening...';
    shortenBtn.disabled = true;
    
    try {
        const response = await fetch(`${API_BASE_URL}/api/shorten`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ longUrl: longUrl })
        });
        
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.error || 'Failed to shorten URL');
        }
        
        
        shortLink.href = data.shortUrl;
        shortLink.textContent = data.shortUrl;
        clickCount.textContent = data.clicks;
        longUrlDisplay.textContent = data.longUrl;
        
        resultDiv.classList.remove('hidden');
        
       
        longUrlInput.value = '';
        
    } catch (error) {
        showError(error.message);
    } finally {
        shortenBtn.textContent = '✨ Shorten URL';
        shortenBtn.disabled = false;
    }
}

function showError(message) {
    errorDiv.textContent = message;
    errorDiv.classList.remove('hidden');
    
   
    setTimeout(() => {
        errorDiv.classList.add('hidden');
    }, 3000);
}


async function copyToClipboard() {
    const url = shortLink.href;
    try {
        await navigator.clipboard.writeText(url);
        const originalText = copyBtn.textContent;
        copyBtn.textContent = '✅ Copied!';
        setTimeout(() => {
            copyBtn.textContent = originalText;
        }, 2000);
    } catch (err) {
        alert('Failed to copy: ' + err);
    }
}


shortenBtn.addEventListener('click', shortenUrl);
copyBtn.addEventListener('click', copyToClipboard);
longUrlInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        shortenUrl();
    }
});