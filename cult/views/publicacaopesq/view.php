<?php

use yii\helpers\Html;
use yii\widgets\DetailView;
USE yii\helpers\Url;

/* @var $this yii\web\View */
/* @var $model app\models\Publicacaopesq */

$this->title = $model->nome;
$this->params['breadcrumbs'][] = ['label' => 'Publicacaopesqs', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="publicacaopesq-view">

    <h1><?= Html::encode($this->title) ?></h1>

    <p>
        <?= Html::a('Update', ['update', 'id' => $model->nome], ['class' => 'btn btn-primary']) ?>
        <?= Html::a('Delete', ['delete', 'id' => $model->nome], [
            'class' => 'btn btn-danger',
            'data' => [
                'confirm' => 'Are you sure you want to delete this item?',
                'method' => 'post',
            ],
        ]) ?>
    </p>

    <?= DetailView::widget([
        'model' => $model,
        'attributes' => [
         //   'id',
            'nome:ntext',
            'redesocial:ntext',
            'endereco:ntext',
            'contato:ntext',
            'email:ntext',
            'atvexercida:ntext',
            'categoria:ntext',
            'anoinicio:ntext',
            'cnpj:ntext',
            'representacao:ntext',
            'recurso:ntext',
            'aprovado:ntext',
            'latitude',
            'longitude',
        //    'geo_gps',
            //'pesquisador',

            ['label'=>'pesquisador',
                'format'=>'raw',      								                                                                  //nome do campo do atributo       	//para nao fazer uma checagem, forma cru
                'value'=>Html::a($model->pesquisador0->nome,			                                                                   //OBS:: crefitoFisioterapeuta -> app/models(propriedade usando como se fosse um metodo, metodos get e set's)------  crefito_Fisioterapeuta -> propriedade do banco de dados  ----- e quando nao tem essa forma adiciona um 0 no final. (tira o 0, e coloca s, e troca tbm no comentario inicial da propriedade)	//'value'=> $model=>idEdital!=null? Html::a($model=>idEdital->identificacao,
                    Url::to(['pesquisador/view','id'=>$model->pesquisador]))	                                                            	//link para redirecionar ao clik, para usar a Url, coloca:: USE yii\helpers\Url; e caso n ter coloque use yii\helpers\Html;
            ],


            'img1:ntext',
            'img2:ntext',
            'img3:ntext',
            'img4:ntext',
        //    'campo1:ntext',
        //    'campo2:ntext',
        //    'campo3:ntext',
        //    'campo4:ntext',
        //    'campo5:ntext',
        ],
    ]) ?>

</div>
