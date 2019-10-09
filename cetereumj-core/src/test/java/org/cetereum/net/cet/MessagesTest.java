/*
 * Copyright (c) [2016] [ <ceter.camp> ]
 * This file is part of the cetereumJ library.
 *
 * The cetereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The cetereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the cetereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.cetereum.net.cet;

import org.cetereum.net.cet.message.NodeDataMessage;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import static org.junit.Assert.assertArrayEquals;

/**
 * Testing different kind of net messages objects,
 * for example, to clarify encode/parse reversal match
 */
public class MessagesTest {

    @Test
    public void testNodeDataMessage() {
        byte[] data = Hex.decode("f93228b90214f90211a07d303f0cb6b53aa51e03134b72f7447a938e3d69d2a1e2951e88af415cfc54bfa0aa37b7de2b35faba0c2c1d328a39e6976631bd91db766e9532b62232c5fea3e9a0d6a6930b51581cc8a6aaab9ba9baa8d297384c9be589acc4a38c1965b2ff460ca05a7b32211d014144e075a338074b1613013818a489ba644d407c387260efac7da03280b01c75a2b8a4fd820bf2671d999174083213500e9bef74f309c378803216a0ac99b656d4ecfe0a7478a7e196784ca6336fec066b9f1960e718d596c72bbf5da03a2c00bb9601c0875e3dc575126679a73d495e17f295be86c1edfbb94441b123a06045ce199c13095165961db9b5e25965614b4c22dfc932d48e545096758b6393a0d60d70c8eec63dc4b20c5615f48e26ebad476f544cd649a66274048fb83ba4c0a096d9dd5c0a7b4a1b009d53d5df94fc4bb165f4b8bb0998942ce0cba9439e170da0a8c743996990a573ae5a8792e08e1b96c44cc0e039374114473b891a25fdded2a0af554afd269d8e1362d792aab0d74c774e7d708a4c94f230aed495389b6a9b68a02d2569b97a69cd4c48db74410e8d7fcca8b103f96fff417de645d3a46b5d5055a07cf48a75c0ea55d3e1ce5d4e43e2c65eb963f81f8c4a79e668156a87b6a15199a01bccb2cb8464b166543b2ff7a9d6f23ff3c397b342e735a631e93badeaeb6f1fa0e809a6ddd5ba566a0202ff53adcd92bc36562aa7f25fee25bffda90c4043e69980b90214f90211a07f228060d35e8517981a6bf48de0cd70adc218183fc1040a6136f8a244000c19a0f4c1cb083074fa68d85ab2507ab46844c2c3d5b4a1cb461190a5472935030570a0124af13bc8c6690d169faaa12b4f77edf34c79ea40955543990ca2fedd3641d8a046eed198f80c1b57bd425e42311016aa49707e5ab26f85a72ee21b2731f51d9fa0f61f057121208af1b642157132296d961002c52c74ecfed11a2a1faab514bae3a0a0b4bf520598f82ea43ee480532adca5c3d36607d56e0ac2428a7918de3c7da8a03fbdc59c5b4c46a0147aff6616ec0f573873523a14b75d7e0beff5015a850742a03d6a13d4627b24561f5e34c9716f7725450aa75214231cd41a42a8ce6017d8dca0e8f7fd0acaf7a0fda7f4ec57213e48f284b9865fd34c39acd73c57d7bd363ea2a013b78164e09324c237aae690974b2bb9410a395263e1b3d8cfbd510df2136860a0b6f0d6bb4218ca4e2499d813895af21fe80afabde38fdab1b9930f5f38fd20e6a0eed4626964447c8e9ce41441fccee2dc9e7154ce8e24dda9bfa0e40292907e9da048ead94ed97b7b0ba8b5673520124046c7bfa9086c7a1a1632e201f35976b25da07a0f8d3c8c2587f2e2cff32e5a21eff284acf6e84c6af592f7db63c2555547eda08dc68f59b02dfc8f5405951948906615b3df77b192e96c945fe3cdfe235b5b1ba0d0c2a883f786349861f76fe5f8a573ffc6b039f8effe1db0479ff57d00a6ff1080b90214f90211a09ffb85b047fd0d70603792e1871291ba8959ad9ba38f6f3f996d701cae14293aa08cd707c87b413b32f7a4133c40ddca9dd0c94fc20d0a4957fa76cea45a04512fa01900e10a8a120f78a52471e6e60a3a882328a051dae508e16d9315a552c1ae20a03c56a9cb58c180ed7320351f21eca3aa88603dea4b32ffa08f703ad694791475a03632993a16501e50253ae47734305addeea31b05640f780de8eb525866e7f2c2a047d7e79e61197742aab166440d1060a8bae928f9c4a4d72cecbeed2dfdc1d5e9a0f7196328f97e5835f9a80808d5dd7d43a9b702dd12f8815700ba89645485856ca0c80c1df1a8e18bcffca5774456774372be138e0be79f9b6d5c174ceb940989b3a0280d2a31d1407cb73ff590eeb9c9c20e60d79a21a99d50a6fbf2d07dbe25112aa0abdf6514623195ee5c17af43e18b0819931567a75b9e76325c86934f4a8cb39ca0e2b5e99c4a7473abfc21e1c00eece6936fb7f39eea94ee0eeae381553f33007ba058b39222b273ceba9fd317e2db88e1773324e8689a56aa0d22dbf0f20daeeabca02daafb2b82cc58f04972783123b291466034b90b0cfcbfa4d46958f8289dcf44a0e035cc41ea4095525ac75a0d96282b9e9b66dd858cebd34bc7f8f456fbd41159a0778aeabebd19c15afeaadf00a16c729079aad3404ab474022b96fdad4d0aedfaa08da34aece7cdf813b1559d4e958ed6add676bf251132711d8c0b46484a02d1bf80b90214f90211a0b29265f3668c7c4195a9b2e0c1a97fda3a9a920ee99e67dced31c7295615319ba0aaec178596b7cd8ae5f8708362e62c7fed9cb34843ae83a238959ec7d6800200a07472b9efe8ac40dc95f09f92d4e74065cb344874824d882c0a10126315784364a05c00c65daceb0bd26382845eff953099022d5a2377259530145753a14b87be16a0bdb04f816249b663ff1cea12123fe2f2612348d5d33816a0aea016e473c5132da04942bf1cf9a79a18a4ba0b2228539d6e5fa6c213c66bb028e567a68e311d4ddda0e98b5fbab8fe6b9123588c7ad46f958fb79527c1c695a5060eaee14ead1c2a2ea0ba7a2303df43f2fe558ce599e7e470c3d1b447252d019010532453599db2b10ba0951867684672973e0901bfde8baab90c093293f807cfb68722ab08eebce921dfa0d520633a15034d22f12b2d04d9b5c6cc1fffbf7d3b52170e092a511a18fca999a0ca85ec566b4eb8aee3c7496d742706b85121e8f1e3d950707faf46762f868e40a06d091294b7e94f52fb54ba7ca7f182e24623033526b1ab9502fc885263bf4672a0d7bc486600fc5cb5e1376807cd7e66725e7761e028386e7463748b9e2aa2ce9ba0b51a3197d51bcad183a1667a437a5666cab90ced7870cbaad65870452f041a1ba0359fa26fc937a5a65ff6d6fba23a9213d86f8c9b5481991fc4b8bf714fd55048a0b674ef300d5955c149e2de3aba1fa228c0d148674523bddd586cd6b7360f4fd780b90214f90211a0790eaabb9dee7eb1aaf2af28f594eeb4a7f467f329b4fa01d5b617d15f40a5bba067eec836cb9d4a4793137bbe8ba2c8b9db91d46d2e3b4bffb30d290a58ea6ea3a0172f0b889aefdc2495f2e00b5eedbe948ec4776df299bc2b0b058bf6a72bf197a03533a9d57193053bdfaa3c097ca0b49e5cca8501e9d37c229762937ed27f6d06a0f9f2eb5a0538a2ea5552e8858d3798309c8c3c13b30fdb02a1d5d65b5d432caaa05b99ff9a8a0e15b948496039dc92bbf5aa67c56f9ce6c8ff19963179d8481d44a0d1bb632d1bd3879f4a00a1cf8c9e904e58e637f087bf8e5d4813d84ff089bc7aa09ca07525f6184961f5b7e195b78f580c5428862103c7fd6aac36db0a3108c075a09e2bdf35a3a11427810c048ce43c8a8699aed99e4b5f1e809251d68a91b4a45ca0a8c5baed3016ff5d11ce39f4c81a223f499cf1f238ea225d52dc8f5bad705ea9a0598d38a92711979c69fba8633724bb50c3fa05b6c12897732d5a483696b3967ea064a5601e5fdc1bdecbdf258d5f5d1b71d54e89f48fe4228240eec07e831d42cda0f5245301cc91d72b1a659078e59b0650722a7de7410f96a682c9e331ad524040a0d0f7504488cd19c08fefc6290f66cdc7fe50968b54804d1a6aabfb09907865eba07cc361cd333675088d372d2a1b62ef7c4f3e5c0df75b9348fd39e259a4c7cba7a061dd5cb5648074c5d0f15370f1d96f5499d458212419a966558f1f2426f87c7180b90214f90211a08d92bcd188b0f7450933496d343e1f0f71796d0db0e833668dff9261b9933b50a0a46386c7de7332708c1d74efe1c394837e12f7dea7954474028fc816aa754077a003888289efbb5202d5be23810bf14f35573f91c71254d0125dfd46320dc69709a0e6a2e4fb43a1b3a4ac841daeb2c7284a8618738be0b2d368b221b951b59ee629a0ae9b2db6a4177c7a9df77d6d1d606e6825a53f811bc1bd94772c5e130c80fa41a0ee368ffc3d5f6746d78f8da2639d12d2aceaa37b4546578ea7d6523d41b0af2ca01eee9a54c7a9e31631926306c9dcd1f3f3464960860facefc8413ae1fe0beb0ea0c3981ef7fbab1ac978aef52d28ef8026da8c3d97c339a89058fabdbfb2f0ec0aa076e5b49d3c2d578732dc429abcb3b8a0e282d541c01f465aca752574851c6869a07d68b3b2df3e1500ee4b2530a2b936e6fed9be2d5bcd77e4ecec52786bdf6a72a052fafe97bd9025c2e08011b3036b771c98577d1e2a7245f3c7aa93c183239a20a0f78075a14bca20652a33c9c8e90932852558fa47991632c68a3f2d93f5b25823a0fb2a4581ad9fa79c68ea5eb73b296143894478091790ef8bf5f26e4964fc81dea0813d838d9f00d69c3167d1679659b17e4f44806842111fcabe3cf2441c8f4981a0070f83d8cb645c7d055a54eaab2f536c02fd93384d79d050bc0779ad3561e01fa0825ba72c1d04a0e03e9201879f6f7e2d11be24bfe93f4152ca57e1e07b93716480b90214f90211a0daf446f6aefe7a4c85f7a61414fdb247fa23ba640c0321abcae97eae1420948aa059c7ba791300a633da935c1070e6719c2979e8540c8ee56403ca68460fb088b4a0d286d661656c927126dcdd49c7c4e7da37e3d9e3bd963813e171f0b673257e0da0643f7412235550772f49fb8aa58330dccff9553b4a501521e7feddb71e5e26c8a092fbcba3021bc2c0f1360179df91e3f52fd5e507abcd5426b6c2dd637ad92843a060a1a4294b260613b5fbdd8a8f6e3cddd7f061feca667d8c9f62931d813bca44a0ba9c1e6d2a4c02a90e5bccaa67a59db3916f5b8dc4ab099921546d7e712003a2a0483d07371d08ba671c357b38a09f920dce09fa7638e5d5200f5df05e8322d9eba000443f99d2394329e38a7ef407629784bbcc1e30341d13517b8e5035486a4740a0c12b6206a18a4bae50aed7570ed41db9d1c8acd100fe2e83f781d2d5ebab54bda05c177f138645abc22a6caaec5f81a5ab8501bfce3b35ce0bbb45d2ea9e476da9a00577616999a95b9cfd950b47b6f81dd1abc48066d063cdd8408b019df7bc6a6ca0c580d7b33cf8612a7d3ec0780b93bd6b6792d69adc5f615dc1997f954a479bfda02a6f0b50ee406091c5b2f4812d7fe50c010d72696e7a43644a27cc8fab8826cfa046014d6e17f59c2fec43538ede5c1d8c9437556c987c922488240f5aa321b5f9a07d722390054c0b15b4deb8399907bf2bcbe20837de0c4ff88cf8774afb78627f80b90214f90211a06cf39d020b1f1aad34330f8a4af94881c6f76b5f562eac95d555c54a4444659ea043a043317a9925440933093bcc239f4bbd555ec7d8a5d5f4909f5928f9f0bcf3a099c5edc3f8481360a917b66603d510eece53f50b26f581487ea511f2377fa7a0a058cb0d9208ad696ed5c1a48407b2fe53d93499932a15bec72452115b58b10f31a0dbaa5d44ed8351dac170d77a704d5cdf5f82aad38c37f82cd50ddec97408b0e8a00c7ea7dd5e15a62e5899d6e8771602affad1cf06caeb61df2a07f2ac63b6dfe6a0eedc9934dbbbd785ae9bc86fca46fef075a5aeed70a1397e3fd819053c04c92aa046302203393c116d5e24b09347a3f7524ad3d5b608334180d5f9903bfe5d9750a098e523ececb1ab617dd2981bbcceac4d4bc2e3df2427f86e5b7b3b8b0ad0c214a01a64e73d23772312515a58281703b3e2031445d26a61e98d20d7bb3ea471c57aa0e7262b834c14f9a10f0fe4d8a0acdee29cf3fda9a21d2395fba8e32984112847a0683aa9c85aff5ca22ffbf95302b675e53a55ffc25433a3492c05977dbcbadb8ea0c09f64a37c8164e9c44485f0a11fb2849c21ea5c7d8ce9c49d1eef38e5fb2ebca084befff1e422326ec61749e2d0a49335adf437b73cb17eccd8f6fd2a4c6a3e2aa0c0e95224af20aa75a4b27097745d16cafa93212787eeeb80359c00f80b1cc4cfa0bf655947225c8206ce32f61213ad6a34d21f50ddd269dc054e9886a1f3489e6580b90214f90211a01535995cf60848a1bfbe5c1bf129b67acea44f2dfa59f29d46d845aa92b0168da0d0b5e1bb1518e52cb8ec76345732c892031ea839ade813fbf0cc24dc77bddfdca070c6c45a8fc589483449cc0209933aec60eff26b043fdf658b37ec451f9cdd4da0bee75da3ee07b013b87bc7b6f61bf4146f4a8bbaf8feb7001f331b575e746e96a0967e3588baca3c782e96e5d964f14feacb082e5c143505d1e478cd63da2761c1a0b0687dd7f95f53466f070282f6c55f8250c9273ec3b5473e1ae83a4c1db7b200a0fde2a38a83aaad2aceddac17aeef1b57bb3a722aadba64ad14213b077eae6f33a06bafb757b6e4c57f25e8619028da69218ae173f12f4a3f77c362ff51b0844e67a0e927ecac726657dd33f1fad32d0994eab19c337a5d9ee966efddba6a4fb08ecca0a2e2e825ac35f9f5679dd0792493bee77116fd3437609c7a1a9e5e414e2143b6a03f90cee4f455a05dcd9215adc85f8ff8a50933fb953f8a84d5d1208c5161131da0adb4b45686d5b0e6586f3b36201feeb052f6b4fd989a4edb288c4b037f305d92a0706d04c66a9a65eeb37ecf32e79c17a30774a072c08a45e2b3d332130cb83818a03f329a00a746f6b6cb8a16c8da6c90860ab7b9815a838972f6ffd6c470503ae3a0886d7fd9d9b9edcb715819d442038970761d1981e884490dbc9b54bfb5e2d2e8a0f9c4f005de7df9bad735c1aff2d52bdf66f24b26c2fe7be6e9d6803ebf0d6a2d80b90214f90211a0bcce77aa2e1cc56548ea5f26ad90ab495453a770ca7961426ad18a2141bf80dda0fb5fd9fd456f31de21a0cd1c7f94f4781717a6008b3cff19f2e9967c23eb9350a0590ac6baa1a7a57c53948bf52f6ff6e8bbed700deeb402d51b0971b20d56e57ca0b77f947ddb37faf6274793e3a256abfb3a3a8ffed1ee0c8727d8723ed3e02df2a05419d8a089850f7ebaa57244806992888e13d3231f36796f2617638c9478bb03a0b67b3c29989884a30101fde67c1e4982cf6a354ebc594709fb1113e03c923ad5a00c2f238746335f03c7dfcc4ad95db75dee1a3a3f06a0579e898f3624a6fc8e65a0b92b7336a1b13e885893b1013472c22acdcb034a73dfadf40a72493b448a7c7ca06c1d5e1e4a442834450073e7abf9a7f5dfa68102c08ad4865d703455339758d4a0e9b3d0e64e74f2c6d5811a0f4d5bc476e0c0d648d97fdaf1372baf999b2a9982a0d128a21efc00fb47a57d0295f3d4155fa44e22a4e92758b604d160ee9f93002ca0c3ea80e9fbbf40d76374b63dada5c2e027cf6c3c5c06b420593531251122fd7ca0a2d72d22872ab9af578c10e0b7cb4c6a2cc993ff7ccfb513674845a35175264aa0b50a3c51fd1f74a88835b992263970e8b8ac78dad10058ee976b2097c5333762a091d4f826a8c6d5d4810961c5b130a8e6130772c8e4295c564b32a940ca031428a056e70dd5341cf0ffa4118ead82639bca43ce6742389037852a100d99b09c496580b90214f90211a0c53085531fc32a4355590c51d1da2e06e4485d7fac8370f081b87b263a7f1dffa05cd64c7fc178739fb035d272754b4aa02dd4a4c5fb139b16ed456274759ff281a07fd325669da905f482be278ff3c4c88eb5e4ca9a166af98c2eab5e170e6c643ea080539b3792634c7e75f202208a09b04d3c6de04960ccdab6b437bc033f14ea1ea06cba2d47d37c1db0cf84d8983a33a7e9d0cde76ae58994b1b64bbc29185bd0afa08e8c938b1da36a8d7141300d3096bea4232b3f3c40b385a504fa839f385c8b3ea0d81b80ef2e2bf0794a47371272e75a158c854e2f4412335f4d456269fbf65393a0c0d31104eac7a5e5b2e21808a0dc226aeb24bcab225de220be27e453f82e0ccca0622973d814235f18baf132812358e4299e5cae36d05d2f20a4990059f93c17a6a02a6adf03f3bb6b441e1eb946693885b608fe83cc1b482b6ad7d1dab4a1e22d24a0378af1df25ea36180ef1ecb2b2ae398927bf602ff89d5849b97476a1b5d4105ba061e19078218fd45e0119d2b9527b61d84a6053ea1c87880e3b1d9da4820ad534a08a87e96f453e07d2981fe127f88c256a1e476678081f1015e2dcfd05cbf5af33a0fe4a0564c6ad3714431c0ffc70f6fb6b7fdd55aab20b8dd36220923e123383d5a0b15088dd6b55d64e186e4f647c9effb39d24074544ae319f2010304ec6d0452ba0caaf4375ece81920948bee4a28d83c42ffc7b764cfa15a1bf9198b38fb7b603880b90214f90211a0519e50ecbb144b94f6e2bfe3b26995d7030ec4b4a0ed8055f31e82bd5e42ccb7a0f5596b86791bbc21f813554850592ece83efa2582d7867ed8faad7cf68cfcc30a09ab3b6894b705c178c2728150ea633adcb39d4192886d1335bcb37bbee3b9319a0dec19f675b23a32333d2079743db133931d7c4d819a6743725ad27e197219534a0cbcd41ab8a69e3c6f53fa18d964b7969b8ebce1fe5f01921bb31a8df83dcca52a0c1a7b1ceab13579e962e299fa0e95cb08999f9fb55086aea35c0ea3919450d33a068d04132744b2b807e6ee133efbf57683c21c79c3690d2905a326730bfcf1ef4a05db703056fa80dc3f41fc89613a18159a9caa876b2259a7f40b52f44399e976ca09d0ba10ae5ecbeafc046e8ce7b68909f28f7664f4e35a07df0831f1a080cf870a034ae6bf3f2ac51d1e2a3164c9f6d1aac8566a4eb2c4a6fbfd65d3c247cb7cca5a0bfd3708415951be5c2d36159d8120eae73eb83491f20c892d34d2b4dc3f7285ea0d1d5aaa464d7e086660cfab62da07e8aeb91ec8b9d179d3889868c90b64b6969a0bd86c170c5e05332ed2287a3360831313eadc9a44eec508625d4d4c184a300efa0bf5864ddbb3d5df12e583d91eb8fa6c783e7e71e2a3aa5bc84b36645c81a0224a0000aa6a49e07a2daa7b4cc1410cbf171bf813ecc29a01cbc6e1fab2c1adc07e1a07c3180e026262363bd6812e98ec3bdc4a2529c697fb9399de8b4ac280a3421ef80b90214f90211a0df620495988e0d6bf37924f0b27760d72dd13247e52be4f385c71b5ad68c8c01a04b38c4915e1767acab09925579c0f1c038a18528836bf88b65a69b13e7745adda0c4233e35236b19fefc984c55b8da459ab820f9a27f7d7ef0847d2d837881117ba0a8739cc31943c91611689fc617ddda7dbdf565aae47ef7b2f02e3a20c1150204a09fb34f35e2cfe0737d7e227f0b0e813768de03da537622416525af71de72126ca05bf43b0b6d6538309e2cc7f97c980a8ebcb23b28f480d9e82b013192ff30d964a01bc45340f94f9423cd1e60dfd4be9cf187f280362ea852aec4ffd5af7f64b043a04e283b36bdec0eb61884bde97e0a2b19bd03a10dce88fc92a7f52e30b86f4ebaa021a1c754b3c3bcbbb7ea0dfa40f9f07a731819075e503de9f62d661196d19432a00d7e799cfa76d57b335137e5e5f87a761ea4c8d929db8a452d5d5f61c309e415a0a22bf2ff3d3705f6246d79078d828b0d93367afde8e70ab8f342a5279d80b132a092fe9c18e7437494d6033f39c7af3177de8b75b328a87f18aee8518045802fe1a0fcacd6a66f09c520db855d7b66c45a379ac0f80418d322ad039efb7ae6c79419a0b30c28009c661cec7478e5cc0582e23ab50463ab094aabc2da09a9d6507c54e5a071dac0f503e43eae74fb8acc66d6026eec7d537563d5db62653d9f97be4fbfd1a0c4195b0ec9cc846b0c9ea0d8e934ca28047aa70e684f3021ad925ddde27fbd7380b90214f90211a0e62b34c602549e0d2537b869fcd80ba937ed4eab20fe630c3a008d030682467ca0107dbebbc041654b5485effdd7585356c50b821aa44aa443c8aaffc493887fc9a03cf4f05885a495d4d1ec70f46eea09b001b066dc6ce7dc88bad26b36e2474289a082b7abb3f626fa2c3f5f32fd7323f538bf691c1c5a549c43d3b6a818865cc1bca07d2f57d940be8c73332534cb773f2e3433bbf0f34063fa44b72da69ac389b2a3a0f9e1a36fe4b7dd7f2ac5d5ad7a38aeda38b998debae9d3d1ce9167c705875ec3a019f430d8293eca48d7c309cc62aa20c8e7220f4bb5efe0424403bbb30e56ef76a0789bddba918171f9d918618978c7df31456f7b34b9c5aa14cc33274184f349fba0eca70410cee4b7de856e8a704d72dabd2dc51e6bb866859ea549646ebf154687a04f5063c9ab039ac16450a8c0ec98d6aaca7ead5e46df5b799d72642aa45d71c3a08ad6412e3dcdef56c471ff7d96600cdacf8ca5321c4d3cd265a8d8e5981db153a05ffda09715d6e6276242fd5b501e9e038aaa48fbab8d6757eeace07f5ed97cc0a008a3d4612fabbb512ae0e553700d16f9d488c1eb86c95c8122f86d59038af5cca0285ded4f9195579adb4409304241a06bff40d91d1c0b0b923f6bade605d148cfa0b9bb0b147ccdb1a3517936b0791c8f6d9989c2db5f9fe5fbe9a1ccc5d1f96e76a06ef4685c31c0b9ea0157b0fd7f7f335a4f4771b5abd3dea3b12570962130a83080b90214f90211a063df6a0c5f3355b31f6ca985183bab8cfa99071795d4f546848c3d4381faa048a046989783fa4e37cd04f7a55a762b2f5ae010d9098887fc3d4e667d8a685a8424a0518d105caf3bc5fdbcaf1e70c195562c634dc224a31acda6159d8ae07bdf1eaaa0d38ba36b4609188f27d5b7d01a29421e36cfd114b50c6f500dd58ddd49273a13a072a97336bc4005c20d708bc40331a843f472ed2652006b4bab3515f797534467a044017b8207927cad145d4a13e863f074ac34f5e079e2331e5a7c73d1012b4b5ca0183aa0e3eb18193ebec3e6bb524bc2a9b6164e6385108525404b7425c309cefea01720723132fa880c138afd13bb56928b2c5c03dcbecfafd2a716c3ceae59bb03a0a988a8b774f01c51e77ec47fa79faf06e11c296e6196bfe8904eabac2e8b21a4a0f55e51f1972c350443066ae8b91d6dce430073c567b22b47c8ce2cbbe1b4d5d1a020a99db9a56f0cece01c235d3817637ea57985d8ad0a9f1bbec9ecc4d1cd55eca0b2b94ca3f92e8519ff64c8b0767d20f17cdd337fd3bb230f6739c6967915853ba0f1badb8cd5fceba2d445251b329daf9455091f66a571b698cceaed73672347e1a05b2bb7144350c35d5ff663d4ccd7a9f76d2e4816981a05e0d59c773836aebdc6a00b7074b75d12000d7602a7f4aaceee9d772c16126c1fbdf42ff981600daafb23a0a61171fa68eb450a4218969ad699883a084aee8aa7d3c46ccd95fc1ad2dbe0ce80b90214f90211a051c51aa0073f05e932df75af481d2cde83e86fcb16f267e27fdbb94dbdbea508a0532e9ef84fd5e0a784b761b7c8593021738e07860321896a8804465777b96d22a0e2ecc9660e412f9f72bb3dbe0f2df9d426fc1476b5b9f84e917ae07d5a8aed0da09d559c3e97e2508db1fda8df5f7b6d3f92ce209dbd743706a1b14fe0dc4b87dba097912a92bb6d97faad42453b71bfd900f3e9b120fb9cd597b47fcaaf5af771d3a089c73b620645de9c0b3435eda42a3c63cd035eb1f7aee6a22992326785bd02f4a0bc22e340a3e467989c1ae2d8d96daebbf612ff1b1269e5b761ebe9833f83828ba057d4130a933b961fca263500c7ce7df0fa0522fb07615f1b6eeb9627575521cfa0c3e67d0333dd2b6bc32f92e3a09a368e71517de41e168e833ac1b829010e45e1a06483dc6326f52c10c57aed477c53b83bcae2f37db7074100a53f4aeeed930996a0eda92a8682840b924990b289daa52d6987be148f0517fe340e622a75feabac0aa01fd9e5069e2ae89646a3e85e6fc63a14070992e9196029bce4ea6737b2ccb582a0b665930ce712933eec2bce24eff8593b07a2be23bb1fee56cb0c58b9385b7f0ea0fa661e4b169c16a778938b9d80dc1a3aaffec57441de5086ba44a78ed3e1a1c4a0affd8a2662dd8576ed2339e00195d6ca75c67a805f55436cecc7de9e149a7a4fa0cf97e8a84017aa4ec509db2afe7b926bbab83b1e63e43b4c3fd5a13d20c3c2a580b90214f90211a0a4823c1b2ace6e9b0951662ebfa2330a6e8403cd67a54a6e74d05bcff21bd9fba0e775f21d473c8354c8bb70e5ea2e4339d1383aa1c31ed574632f53019bb9c27ea066aa3df101bdac4f5f9eddbf4718e27c36cdf2eba147c5ba79ffbb249023ecdea02c8a4e078c9d4b955a5e2bdadaefb91652b170802f489e9d73ac3b7d5aef722ea0599458e68f8821dc4b4747efcb451e5bb63c39f86d5ae2dceccc3b642a1b2065a03cbee948f53025368d047076cd6ff2b2f0a218c0ccd10c637e4abe22121a2067a039e1dc89ead629b6460cd49aa05b7921557abf3e04d9990f239b84ccb0155b05a052702930120fe255e4b24923b8a3c865f4bbaad50d13712c6e8a64f1802b97b8a0374710d55d164e417221148ab6f34aa0e090b3d3b5e3080347dfbaf44abea053a0c83880d65345437ae11c5217df7fb9a3ee8b3054c94424b93afbb79bbd0d81bfa0561d415e31798ae6e447275dab3afb3edf37d9d5eee655b7cd622240d13dec72a00f6d2dc43a193ba4e7bd0885959a927a067cb393fe8cd74cf9540ae76142dfd2a0e8f4fcc6fd04ac7f159bf9d674fef9f4cf3ccf76ec5c2eac53a5c209fc5dc03da053e838c8e6770ad4133de92eb6dd7e2f08f0fc5cc60fe55c68a9bab7dca9528ba038c1c632ab1058083ceecdb00bc6d13a1e457afb166b411551555126914b1522a0ec2e10b30b861899a819c9be179c25d17d2618f21ea483f6cef1048685866f6280b90214f90211a04a8437f5c35d03bb0f8a741dd35165a3ba132ee4d92c701ae3c8d4f3d65e3e49a00bd6904465fd8035357a63750cae05707e844a1acb5cc0b25468c20c90871e4fa0f320d73efaaf4c6a68ad6167118b549bef6844feb1762ea89e8758b967755f33a0fbe27008eb14d98c309125f47854636e1ef033fa0d5c37ccce89771f6ab7f582a0c74e46c4c62619a09a045ad57adb4ec3de037d029dcdb18d2678d4072d243997a017ff8546a33be106018e5fc483fd7e8585bd2ea126fa22a8a6f6904ae38c334fa0db17377119d9809a4ee6e957077fe57898cabb89e3a162e9c81b21cb50c0ab2ca0ffc816f07157a6683c583e7a277ddbd420597cdc88e2702ebf5ccef530e7a784a01d532e49c4212a4ba56346708347489ebf8569fdf5d9aee201a100a1f03d0c7ca020012a52d3dc1f650a6b5982e5ef496d4fd0dcfebddf987abfb245797a885226a0d89e27e7afbe451339afbb0ab8dda61ff272eebc961fae9aebf8efeb02e76caea0bada48b96bc636c5409fe8671d3674948dcd5551121e219590e9a97067dd1950a04791e8c4db73ba380a6db843bde19109c14aa54ebafcc8788fcf70811808a44ba0fbb127d6d3a4942c9c1a274c4b17f397fcc7637a00e7c72504f1141259ab464fa0760ffccd967a3c281e3f4e6a663ad2aa4fe9b1ad684ef0e7cbf57b9153419c0aa08b5bddc52a031d21e215aaddcd6f750457a121c8d33d25aebe85007fa28410d980b90214f90211a0ab7f9777fee3fef04803a2659346a83e975f2bb4b29233b1135d6974531424a2a087b6be7fa8c65601f0458fa21ab2f46e4ba6b04480616e136a206373f203d65ca003d038b1a4609cc7d325a6d7f851c29e491f2b1a67832eb2a317322a010511d5a03930bd18e66580c73cc5926cc57deab76b04857c5ab63c6a9b30f6e8fc82b0f5a0343a2761a59a0e40148b94bc2df522b24983d71bf440fd570cb470beebdd2db1a00901f2c4aa4bab6fb3f04869adc2aa42b8c864d99e90d508d0d8f875c430f67da08e30cf6fa8512d687a8dc31c6ce02ab9227100a8e63fb9cea9bb9cb524dab03da064d53dcabbc6477b41f2f581bfc4e72bb097c805e1b81662f689a821cfcbfe25a05cff2284c06a2206660ecf684cedfe1e46eaf8d93b02818bd852996460e79175a098c2465d4c6ab5818167931acf7d8458a6868e44c875a8a957bc3bd302d33a0da05ddc7f1a63c24ef3aabfbbdad6f9c8ee5782faa554c2ec5da629508ff0252e2ca026887eec68c07a32d428ea8753cc12c8ac9eb8884a68dd89ba90e99c528c4e10a000a145035505187db0fb013cf4664a3d83e9197d30da170c2779615325beabe2a0411c1a6582428012cbc575a8a5e62819bc644f0c925cfd8d9f692889d41cc38ba02e68ba05db68da35dfbe9bc6af88b44aa49cc0b2dd5f2925fd2dd007bdfda7f7a0e85c87ca7f7b42d43d418798ad0de2aa64ca281c7b7cab3b66e17c4f595d1fab80b90214f90211a091d29c21c9ff7416e2e7314f5912e2e6209a99026e12ccacbfd1b498ed981076a0e15768ca54a99f80276a1e023c7a36a9eb3106f54c97210e694f0d090f17173fa06bf3544c939680c6c422185f2facbcf0afc3ac29ad7c1b69078899f410a0c710a0f9c7b8b679bfb81b1d1199c7cdbda437a3ba66daad5d3f1ed9fab5a9fc97f349a0085f12b4134e60002ece5215bc3167dddedd2bc384c0372084e1a22ae202a655a06ecefb6536d152f4e3788f2ee4f1e9e7a254c6506db3ed44d8709b259a4f5fe0a075de2affd5601392de0f42964bc82887090a33c4709e4dd4a72eebee53bce929a074bcc926b8e4d5be57b27b479e255d9322d038b2e77e7676724fc7b0efcfc74aa03812659ea8178131121193ee1ae4c30ab054a8744e0c63d2081f910e770fbc21a087e47b3e87d53624deb9178d2ded0d6ba6313488f2d1c730906c47bf1d1d3281a04eee770df3cd32cc9321de094939802f5e53ba223bacade769402abf8cf91dfca01f8ade399e93c68c8ef2703a9d41c6cddbd6c3d04d745a8a5a76ab61bd049527a005a4bef0743f40d8dee65799a6b8efa044f304709ca44dde2d3ccc8104abf651a0c6a8531ac33aaf86109ea56ffa9cbf4517ff8d37f76520da89437150abd77c4ba0ace333416d0dc931ee3a78f7c2386201cd312b19c20c6b9c57abfa313077b9bfa0548c28373c9f2a643ef5a50af13b24f340dbd9e8f34d6502bfb1e5add3ebf28d80b90214f90211a080c24e029fc0f8ebb93b6d6bccc328f2c49ff474159e7e3e9827a207035ebb35a0c9feaaa5752601a286b0cde72242d2fa36ffa4c94412d6d5b6d4ea5799461af5a0683949a9b5c22c62b06dd9922730ec1b3e832a514a02c6ffe380655ffe99c77ca045c595e624971dbcd2d97c594256bd01028a7ee5e550c5b0ec7502f5dd95256ba0ca296ec5d7a49c541685c43abe1619b1e80ebad10772d097a3373de1aece72b1a0f2843d3f1c0e5306766bfb47be6e115dbc68c840464ba8761166abff7be91a5ea000e449032d65048440433012666e546982ee09177aa9e900337abbe4457f9580a06a5b5fcf57a1eae3d7df0c4802b60b42de2311f1fa23a1c979396dea8e8a8974a00e209da4553e88d2710459faceff7b244e5bdf585df0d5b4462af76b28761954a01ac8b8bf470510a3488e8303f68aa3002a8d2902817c7e82f5e51d0b9147573fa044cd7e841cbba3859318743462e833e258363b91c99ca0873660a96a6985a1d6a04c5b8dd32d19f61ebf70d2bdd6b81ffb3d4baebf183bf744f4f107cabd707549a060e423d7e6c04aa60df0791b20bf6f1384e7016ad73f91dcace04ac81696181ca0368c26079c1659d06fc3979e74c1fadc91fd86c133c2b0b60fc1680a63afb908a012442c17735a2b4ad90e0dea4ed270ed04d743fd373043607d0721b0a4c20f69a0d04973872715327c4cff70af4fa25cc98aedc6019c78bfe7bb5322e7be442a0780b90214f90211a02003d028d7a9952f3537f1bacd5355f9cd15ffdb3b0320465469ee0bd63d8cdfa08ae64938d7e196d11df9602ac8f51544847a288278ca5139ab56c8c3d8c4168ca0c6b1974b09b1cf73a309e6c25dab139cd51b90771af2683d2381b8d86ea2ee55a0d646da3405c7f32a3ee3f1ed0b39e35a751569c59238faae3d47e5d3de1fcabea0139a453d2776bcdd371adb7a17314d9a8e8b9cc14b592ab01a06d839aa87e89aa072a84d2d5a35b6cd6dddc422da763360da79fb558e00131c074a85eda4abe9a1a0e17e40d1ef509e40f0dffb0a63c5079bc59c89454222b9cb2b98bb1560bb08e8a00ee17c5d8efcbafd4e019e1ca3bb482b6f0f880956587a202c67b4af06359d7fa012d62d4c477b2222497b946546b213b92271b86d280cb9ad13e92ca3d5af4be0a0307c34d06f6f3b097a0407050aefad6ce44a77b204f968b96a5be4318dab3854a010c723df1ef0bbf80f0c0f8d4959a5394eec82d69f8685eada1e8e14bb94cc80a01d4e35a6bbe6d381b7a77f54a7fc4416f242a8d0f7574139e86763b9dd5f6b7ca0e4ce8a2aa1901506dcc0a355180be12ab9ee75f34aca311884d5fba395b14ea1a05d8b0c593e7f79dd15a71355a0145b1b0a318b532a51317e8ab9850aec954a66a0faade219a3ce432c2ec65bec1ca692fc879c4875ec2c00e81489eff802701083a0d2883ea362abca76be4a9110c9bf2b8f46ae9ed204b136e15929e27cc412c7dd80b90214f90211a0c2332f0f15eedca1e1434810d8e6cd9a3f75688c80f7617f241dff87cc0dde3aa0b7e6e083d2a65e27090079da274e01bc1afc91ae247a670fbe8bce0d76b44b8da093460542ccacb9680fe5a79abfcc97db68d0d845b438b27492945dc60cc37486a09e76311a289abf2c706fe8c5242ab3ad3188fdb1c0626a4d504bcad5b747c9ffa0de04dffa807147cb8ba0d4d81d36d7fb95a6e60c6245829db5d0e2cf9bbbddaca026aee6ee7e9c8cd1347e3b662f6ebb225d10d3848059db6305a60965d8609e61a0b269cb09d110523bb117a9934f4fc6d974cc03db5f36a3c35e22938970b203f8a0a555ab1fc84447a85c44c00365fa53cdd626887469ab89a84aa17b823b257665a032400a2e1fc559f2a0f9f9ebc64f09f79c3806f96ca38dbfc1c66a76c26d45b5a01f02079c66611f4396221463acabd93e084bffcac382f5bac2568f1f663eb121a02eed784638a1afebae6a6384a5237928fd0846f7159fcaed30c7430f942f0839a0283461d805fd72ed60627e1e74e518bcf65565aef6ae4093671f1af2ca7f05e0a00524b4ac006d6e11c3b28d780b0707dbab7c47b0691b9b707a2dd7d01b1e7b61a07b0517a176691aec7337930c7a62277472d7514ba142a0a9e41cb4160d16d5bda0c8c8bc1ca8c13493c98d3deebebdc1e1e4865bc59120a0da1134b95c6a825368a0298f42242f7de6281b41e10e0c96a01f9044795ebc1e41345e4988799345dac280b90214f90211a095b9889317a088e2ff979d85893e23cb3ec091a0eced00bf2608b09528d30eaea08e0a2aef31d3817c95ca9561af8c5788e3afcab0783439825a66c1a4210cf3b6a049ef720f106c1c9b15339026eb21330d9bfde8cd1eedb4817b01d34ca36e2526a015066ea9d934b8f486b7f7c5dd5abcd0f87c0585fa71eb70d6e5176d76ec1c6ba0f1c71b8a6ce31478dda7dd74cd9f40be0d81640f2a8e1d19cacc3262c5adfa4ba072637ebfc507d4d39f4c9936d415febbd2fd8bb29c6e47951c135f1e926af9b5a050cf735290e3013c3406a388f65eb55d2fe19016995a9620352fdd2759260355a01cae5c026e5444a2d8a10ebc11973e14a1d687f9ef9547c4878020b85fc32df2a04ddb98a0ae6079fbccb2836c9f30b6fe2b71fd6ecbc53fcc3d662f7f65c2ae70a04c972c242ec2b42b94ce4cff4d67ca3ead89ed4093c2640c37a0ae5c8c402ce8a0422f91986f6c6b0763f88a8efa354cf97c160fee718ae16b94a6267834c539f0a0b5b7fa471f9e267d44f42ca23b05e7577d79a708b586e5f3bfc91cffdfc9595fa02dc1b9d365a2d9b32350e6df9a1a72d7cd7f288040254667807f91e1d4b37f74a005d3dc75f43c404dca66b03fa5bb26cceb435a55b065ab0919450c2f83c30fdfa05321c6545b832cbf31ca6d95b7b9676ef92d32bd65b151f2d9b410f85324dfd8a0fb93825b957d2ec4094bc6a82b1261687eacbad17cc4fae2cc62f41263a0bf0380");
        NodeDataMessage msg = new NodeDataMessage(data);
        NodeDataMessage msg2 = new NodeDataMessage(msg.getDataList());
        assertArrayEquals(msg.getEncoded(), msg2.getEncoded());
    }

    @Test
    public void testNodeDataMessageEmpty() {
        byte[] data = Hex.decode("c0");
        NodeDataMessage msg = new NodeDataMessage(data);
        NodeDataMessage msg2 = new NodeDataMessage(msg.getDataList());
        assertArrayEquals(msg.getEncoded(), msg2.getEncoded());
    }
}