// Run npm install befunge93 
// To use this script 


const Befunge = require('befunge93');
const fs = require('fs')
let befunge = new Befunge();

const printUsage = () => {
	process.stdout.write(`USAGE
	node befunge93-cli.js -f my_befunge_file
	or
	cat my_befunge_file | node befunge93-cli.js
DESCRIPTION
	-f, --file     (optional) interpret the file at the given path instead of stdin
`);
}

if (process.argv.length === 3 
	&& (process.argv[2] === "-h" || process.argv[2] === "--help")) {
		printUsage();
		process.exit(0);
	}

let fileToRead = null

switch (process.argv.length) {
	case 2:
		fileToRead = process.stdin.fd;
		break;
	case 4:
		if (process.argv[2] !== "-f" && process.argv[2] !== "--file") {
			process.exit(1);
		}
		fileToRead = process.argv[3];
		break;
	default:
		printUsage();
		process.exit(1);
}

befunge.onInput = (message) => {
	process.stdout.write(message);
	return fs.readFileSync(process.stdin.fd).toString();
};

befunge.onOutput = (output) => {
    process.stdout.write(output);
};

befunge.run(fs.readFileSync(fileToRead).toString());