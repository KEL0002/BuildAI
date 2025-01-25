# BuildAI

## Functionality
BuildAI is a creative mode utility that allows various Large Language Models to build structures ingame. This plugin is meant just for fun, **AI models are generally not advanced enough to build anything good**.
## Setup

After installing the plugin on your server and starting the server, a config.yml file will be created in the  
`plugins/BuildAI` directory. At the bottom of this file, you can configure the provider you want to use. You should be able to add all common providers like OpenAI or Anthropic (both not tested!), but if you are running the server on your PC I suggest [Ollama](https://ollama.com/), as it allows you to run LLMs locally for free.
## Usage

As an operator, you can use the command:  
`/aigenerate {model_preset} {x1} {y1} {z1} {x2} {y2} {z2} {somevar=somevalue} {prompt}`. You can make a selection using a wooden shovel and use the command:   
`/aigenerate {model_preset} {somevar=somevalue} {prompt}`. The variables ("somevar") can be configured in the config.
## Security

Most providers require an API key to access their models. Safeguarding this key is crucial, as anyone with access to it can use the model without your consent. This plugin offers two primary methods for storing API keys:
1. **Storing the key in the `config.yml` file:**  
   While this method prevents players from viewing the key directly, you should carefully consider who has access to the configuration file. Additionally, note that the associated command lacks spam protection, so ensure that only trusted individuals can execute it.
2. **Using `%api-key%` in the configuration and allowing players to input their own key:**  
   It is essential to understand that anyone with terminal access can view the API key. For players, it is strongly discouraged to input your API key unless you own the server.

## Verisons

BuildAI was developed with [Paper](https://papermc.io/downloads/paper) 1.20.1 and is primarly tested with this version. It should be able to run on all bukkit based softwares in all versions after 1.13.
## Official links
- Modrinth: [https://modrinth.com/project/buildai](https://modrinth.com/project/buildai)
- PaperMC Hangar: [https://hangar.papermc.io/KEL0002/BuildAI](https://hangar.papermc.io/KEL0002/BuildAI)
- Source: [https://github.com/KEL0002/BuildAI](https://github.com/KEL0002/BuildAI)
