window.MyFindInstitutionComponent = zoid.create({

    // The html tag used to render my component
    tag: 'my-findinstitution-component',

    // The url that will be loaded in the iframe or popup, when someone includes my component on their page
    // using http://127.0.0.1... to simulate a different domain to http://localhost...
    // url: 'http://www.my-site.com/my-findinstitution-component'
    // you can also call it from the following (not recommended in production environments)
    // url: zoid.getCurrentScriptDir() + '/../js/findinstitution.html'
    url: {
    	local: 'http://127.0.0.1:8080/ra21/z-component/js/findinstitution.html',
    	testlocal: 'http://localhost:8888/ra21/z-component/js/findinstitution.html',
    	test: 'http://crispy-snippets.appspot.com/ra21/z-component/js/findinstitution.html',
    	live: 'https://nightly.pyff.io/ra21/z-component/js/findinstitution.html'
    },
    defaultEnv: 'testlocal',

    // default dimensions for the component
    dimensions: {
    	width: '300px',
    	height: '80px'
    },
    
    // defines the log level in the JavaScript console
    defaultLogLevel: 'info', // debug,info,warn,error
    
    // defines if the container should be resized
    autoResize: {
        width: true,
        height: true
    },
    
    // defined for an iframe context - see https://github.com/krakenjs/zoid/blob/master/docs/api.md for additional details
    contexts: {
        iframe: true,
        popup: false
    }
    
});