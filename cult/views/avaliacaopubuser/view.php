<?php

use yii\helpers\Html;
use yii\widgets\DetailView;
USE yii\helpers\Url;

/* @var $this yii\web\View */
/* @var $model app\models\Avaliacaopubuser */

$this->title = $model->pubuser->nome;
$this->params['breadcrumbs'][] = ['label' => 'Avaliacaopubusers', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="avaliacaopubuser-view">

    <h1><?= Html::encode($this->title) ?></h1>

    <p>
        <?= Html::a('Update', ['Alterar', 'id' => $model->pubuser->nome], ['class' => 'btn btn-primary']) ?>
        <?= Html::a('Delete', ['Deletar', 'id' => $model->pubuser->nome], [
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
           // 'id',
            'nota',
           // 'idpubuser',
            ['label'=>'pub usuário',
                'format'=>'raw',      								                                                                  //nome do campo do atributo       	//para nao fazer uma checagem, forma cru
                'value'=>Html::a($model->pubuser->nome,			                                                                   //OBS:: crefitoFisioterapeuta -> app/models(propriedade usando como se fosse um metodo, metodos get e set's)------  crefito_Fisioterapeuta -> propriedade do banco de dados  ----- e quando nao tem essa forma adiciona um 0 no final. (tira o 0, e coloca s, e troca tbm no comentario inicial da propriedade)	//'value'=> $model=>idEdital!=null? Html::a($model=>idEdital->identificacao,
                    Url::to(['publicacaouser/view','id'=>$model->idpubuser]))	                                                            	//link para redirecionar ao clik, para usar a Url, coloca:: USE yii\helpers\Url; e caso n ter coloque use yii\helpers\Html;
            ],
        ],
    ]) ?>

</div>
